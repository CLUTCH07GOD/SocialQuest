import pytest
from sqlalchemy import func, select, text
from sqlalchemy.exc import IntegrityError

from app.models import Consent, Profile, ProgressEvent, SessionRecord, User
from tests.conftest import other_auth

API = "/api/v1"


def test_consents(client, auth):
    assert client.post(f"{API}/consents", json={"type": "terms", "version": "1.0"}, headers=auth).status_code == 201
    assert client.post(f"{API}/consents", json={"type": "bogus", "version": "1.0"}, headers=auth).status_code == 422
    got = client.get(f"{API}/consents", headers=auth).json()
    assert [c["type"] for c in got] == ["terms"]


def test_profile_patch_only_changes_sent_fields(client, auth):
    assert client.get(f"{API}/profile", headers=auth).json()["preferred_mode"] == "standard"
    r = client.patch(f"{API}/profile", json={"situations": ["Phone calls"], "goal": "Ask one question", "captions": False}, headers=auth)
    p = r.json()
    assert p["situations"] == ["Phone calls"] and p["goal"] == "Ask one question" and p["captions"] is False
    assert p["display_name"] == "Sam" and p["preferred_mode"] == "standard"
    assert client.patch(f"{API}/profile", json={"preferred_mode": "vr"}, headers=auth).status_code == 422


def test_progress_is_idempotent_and_server_decides_xp(client, auth):
    body = {"type": "session_completed", "ref": "s1", "idempotency_key": "key-0000001"}
    first = client.post(f"{API}/progress-events", json=body, headers=auth)
    again = client.post(f"{API}/progress-events", json=body, headers=auth)
    assert first.status_code == 201 and again.status_code == 200
    assert first.json()["id"] == again.json()["id"] and first.json()["xp_awarded"] == 20
    client.post(f"{API}/progress-events", json={"type": "reflection_completed", "idempotency_key": "key-0000002"}, headers=auth)
    s = client.get(f"{API}/progress/summary", headers=auth).json()
    assert s == {"total_xp": 35, "events": 2, "by_type": {"session_completed": 1, "reflection_completed": 1}}
    assert client.post(f"{API}/progress-events", json={"type": "xp_hack", "idempotency_key": "key-0000003"}, headers=auth).status_code == 422


def test_xp_can_never_be_negative_in_the_database(db_maker, client, auth):
    with db_maker() as db:
        uid = db.scalar(select(User.id))
        db.add(ProgressEvent(user_id=uid, type="x", xp_awarded=-1, idempotency_key="k"))
        with pytest.raises(IntegrityError):
            db.commit()


def test_sessions_lifecycle_and_ownership(client, auth):
    s = client.post(f"{API}/sessions", json={"situation_id": "job-interview", "mode": "standard"}, headers=auth).json()
    assert s["status"] == "active"
    assert client.patch(f"{API}/sessions/{s['id']}", json={"status": "paused"}, headers=auth).json()["status"] == "paused"
    done = client.patch(f"{API}/sessions/{s['id']}", json={"status": "ended"}, headers=auth).json()
    assert done["ended_at"] is not None
    assert client.patch(f"{API}/sessions/{s['id']}", json={"status": "active"}, headers=auth).status_code == 409
    intruder = other_auth(client)
    assert client.patch(f"{API}/sessions/{s['id']}", json={"status": "ended"}, headers=intruder).status_code == 404
    assert client.get(f"{API}/sessions", headers=intruder).json() == []


def test_export_has_data_but_no_password(client, auth):
    client.post(f"{API}/consents", json={"type": "privacy", "version": "1.0"}, headers=auth)
    data = client.get(f"{API}/me/export", headers=auth).json()
    assert data["user"]["email"] == "sam@example.com" and len(data["consents"]) == 1
    assert "password" not in str(data).lower()


def test_delete_account_removes_every_row(client, auth, db_maker):
    client.post(f"{API}/consents", json={"type": "terms", "version": "1.0"}, headers=auth)
    client.post(f"{API}/sessions", json={"situation_id": "x"}, headers=auth)
    client.post(f"{API}/progress-events", json={"type": "session_started", "idempotency_key": "key-0000009"}, headers=auth)
    other_auth(client)  # a second user must survive
    assert client.delete(f"{API}/me", headers=auth).status_code == 204
    with db_maker() as db:
        count = lambda m: db.scalar(select(func.count()).select_from(m))
        assert count(User) == 1 and count(Profile) == 1
        assert count(Consent) == count(SessionRecord) == count(ProgressEvent) == 0
    assert client.get(f"{API}/me", headers=auth).status_code == 401
