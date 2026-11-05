"""Voice session gateway (P1 side). Text turns work now; audio frames, STT and TTS plug in later via P3's adapters.

Protocol:  client -> {"type":"auth","token":"..."}                 first message
           client -> {"type":"user_text","text":"..."} | {"type":"end"}
           server -> {"type":"ready"} | {"type":"caption","role":"character","text":"..."} | {"type":"error","code":"..."} | {"type":"ended"}
Conversation text lives only in memory for the life of the socket. Nothing is written to the database.
"""
from fastapi import APIRouter, Depends, WebSocket, WebSocketDisconnect
from sqlalchemy import select
from sqlalchemy.orm import Session

from ..db import get_db
from ..models import SessionRecord
from ..providers import get_llm
from ..providers.base import LLMProvider
from ..security import decode_token

router = APIRouter()

SYSTEM_PROMPT = (
    "You are a friendly practice partner in a social-skills practice app for adults. Stay in role and keep replies "
    "to one to three short spoken sentences. Never give medical, therapy or diagnostic advice and never ask for "
    "personal identifying details. If the user says they want to stop or seems distressed, step out of the role "
    "gently and offer to pause or end the session."
)


async def _reject(ws: WebSocket, code: str) -> None:
    await ws.send_json({"type": "error", "code": code})
    await ws.close(code=1008)


@router.websocket("/ws/sessions/{session_id}")
async def session_ws(
    ws: WebSocket,
    session_id: str,
    db: Session = Depends(get_db),
    llm: LLMProvider | None = Depends(get_llm),
):
    await ws.accept()
    try:
        first = await ws.receive_json()
        user_id = decode_token(str(first.get("token", ""))) if first.get("type") == "auth" else None
        rec = db.scalar(select(SessionRecord).where(SessionRecord.id == session_id, SessionRecord.user_id == user_id)) if user_id else None
        if rec is None or rec.status == "ended":
            return await _reject(ws, "unauthorized")
        await ws.send_json({"type": "ready"})

        history: list[tuple[str, str]] = []
        while True:
            msg = await ws.receive_json()
            kind = msg.get("type")
            if kind == "end":
                await ws.send_json({"type": "ended"})
                break
            if kind != "user_text":
                continue
            text = str(msg.get("text", ""))[:2000].strip()
            if not text:
                continue
            if llm is None:
                await ws.send_json({"type": "error", "code": "llm_not_configured"})
                continue
            try:
                reply = await llm.reply(SYSTEM_PROMPT, history[-12:], text)
            except Exception:  # never leak provider details to the client
                await ws.send_json({"type": "error", "code": "provider_failed"})
                continue
            history += [("user", text), ("model", reply)]
            await ws.send_json({"type": "caption", "role": "character", "text": reply})
        await ws.close()
    except WebSocketDisconnect:
        return
    except ValueError:  # malformed JSON
        await ws.close(code=1003)
