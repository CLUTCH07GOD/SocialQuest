import pytest
from starlette.websockets import WebSocketDisconnect

API = "/api/v1"


def _session(client, auth):
    return client.post(f"{API}/sessions", json={"situation_id": "networking"}, headers=auth).json()["id"]


def test_voice_gateway_text_turns(client, auth):
    sid = _session(client, auth)
    token = auth["Authorization"].split()[1]
    with client.websocket_connect(f"{API}/ws/sessions/{sid}") as ws:
        ws.send_json({"type": "auth", "token": token})
        assert ws.receive_json() == {"type": "ready"}
        ws.send_json({"type": "user_text", "text": "Hi, I'm Sam"})
        assert ws.receive_json() == {"type": "caption", "role": "character", "text": "echo: Hi, I'm Sam (turns so far: 0)"}
        ws.send_json({"type": "user_text", "text": "Nice to meet you"})
        assert "turns so far: 1" in ws.receive_json()["text"]  # earlier turns are remembered
        ws.send_json({"type": "end"})
        assert ws.receive_json() == {"type": "ended"}


def test_gateway_rejects_bad_token_and_other_users_session(client, auth):
    sid = _session(client, auth)
    with client.websocket_connect(f"{API}/ws/sessions/{sid}") as ws:
        ws.send_json({"type": "auth", "token": "nonsense"})
        assert ws.receive_json() == {"type": "error", "code": "unauthorized"}
        with pytest.raises(WebSocketDisconnect):
            ws.receive_json()
    from tests.conftest import other_auth
    intruder = other_auth(client)["Authorization"].split()[1]
    with client.websocket_connect(f"{API}/ws/sessions/{sid}") as ws:
        ws.send_json({"type": "auth", "token": intruder})
        assert ws.receive_json()["code"] == "unauthorized"
