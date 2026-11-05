from datetime import datetime, timezone

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy import select
from sqlalchemy.orm import Session

from ..db import get_db
from ..deps import current_user
from ..models import SessionRecord, User
from ..schemas import SessionCreate, SessionOut, SessionPatch

router = APIRouter(prefix="/sessions", tags=["sessions"])


@router.post("", response_model=SessionOut, status_code=201)
def create_session(body: SessionCreate, user: User = Depends(current_user), db: Session = Depends(get_db)):
    rec = SessionRecord(user_id=user.id, situation_id=body.situation_id, mode=body.mode)
    db.add(rec)
    db.commit()
    return rec


@router.get("", response_model=list[SessionOut])
def list_sessions(user: User = Depends(current_user), db: Session = Depends(get_db)):
    q = select(SessionRecord).where(SessionRecord.user_id == user.id).order_by(SessionRecord.started_at.desc())
    return db.scalars(q).all()


@router.patch("/{session_id}", response_model=SessionOut)
def update_session(session_id: str, body: SessionPatch, user: User = Depends(current_user), db: Session = Depends(get_db)):
    rec = db.scalar(select(SessionRecord).where(SessionRecord.id == session_id, SessionRecord.user_id == user.id))
    if rec is None:
        raise HTTPException(status_code=404, detail="not_found")
    if rec.status == "ended":
        raise HTTPException(status_code=409, detail="session_already_ended")
    rec.status = body.status
    if body.status == "ended":
        rec.ended_at = datetime.now(timezone.utc)
    db.commit()
    return rec
