from sqlalchemy import func, select

from app.models import Profile, User
from app.ratelimit import auth_limiter
from tests.conftest import register


def test_register_login_and_me(client):
    r = register(client)
    assert r.status_code == 201
    token = r.json()["access_token"]
    me = client.get("/api/v1/me", headers={"Authorization": f"Bearer {token}"})
    assert me.status_code == 200 and me.json()["email"] == "sam@example.com"
    login = client.post("/api/v1/auth/login", json={"email": "SAM@example.com", "password": "correct-horse-1"})
    assert login.status_code == 200


def test_under_18_creates_nothing(client, db_maker):
    r = register(client, age_confirmed=False)
    assert r.status_code == 403 and r.json()["detail"] == "age_not_confirmed"
    with db_maker() as db:
        assert db.scalar(select(func.count()).select_from(User)) == 0
        assert db.scalar(select(func.count()).select_from(Profile)) == 0


def test_age_flag_is_required(client):
    body = {"email": "a@example.com", "password": "correct-horse-1", "terms_version": "1", "privacy_version": "1"}
    assert client.post("/api/v1/auth/register", json=body).status_code == 422


def test_duplicate_weak_password_and_bad_login(client):
    assert register(client).status_code == 201
    assert register(client).status_code == 409
    assert register(client, email="b@example.com", password="short").status_code == 422
    bad = client.post("/api/v1/auth/login", json={"email": "sam@example.com", "password": "wrong-password"})
    assert bad.status_code == 401
    ghost = client.post("/api/v1/auth/login", json={"email": "nobody@example.com", "password": "whatever-123"})
    assert ghost.status_code == 401 and ghost.json() == bad.json()


def test_protected_routes_need_a_valid_token(client):
    assert client.get("/api/v1/me").status_code == 401
    assert client.get("/api/v1/me", headers={"Authorization": "Bearer nonsense"}).status_code == 401


def test_password_is_hashed(client, db_maker):
    register(client)
    with db_maker() as db:
        h = db.scalar(select(User.password_hash))
    assert h.startswith("$argon2") and "correct-horse" not in h


def test_rate_limit(client):
    auth_limiter.limit = 3
    codes = [client.post("/api/v1/auth/login", json={"email": "x@example.com", "password": "pw-12345"}).status_code for _ in range(5)]
    assert codes[:3] == [401, 401, 401] and codes[3:] == [429, 429]
