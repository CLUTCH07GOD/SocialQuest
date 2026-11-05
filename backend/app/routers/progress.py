from fastapi import APIRouter, Depends, HTTPException, Response
from sqlalchemy import func, select
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from ..db import get_db
from ..deps import current_user
from ..models import ProgressEvent, User
from ..schemas import ProgressEventIn, ProgressEventOut, ProgressSummary
from ..xp_rules import XP_RULES

router = APIRouter(tags=["progress"])


def _existing(db: Session, user_id: str, key: str) -> ProgressEvent | None:
    return db.scalar(select(ProgressEvent).where(ProgressEvent.user_id == user_id, ProgressEvent.idempotency_key == key))


@router.post("/progress-events", response_model=ProgressEventOut, status_code=201)
def add_progress_event(body: ProgressEventIn, response: Response, user: User = Depends(current_user), db: Session = Depends(get_db)):
    """Idempotent: retrying with the same idempotency_key returns the original event (200) and awards nothing twice."""
    if body.type not in XP_RULES:
        raise HTTPException(status_code=422, detail="unknown_event_type")
    event = _existing(db, user.id, body.idempotency_key)
    if event is None:
        event = ProgressEvent(
            user_id=user.id, type=body.type, ref=body.ref,
            xp_awarded=XP_RULES[body.type], idempotency_key=body.idempotency_key,
        )
        db.add(event)
        try:
            db.commit()
            return event
        except IntegrityError:  # a parallel retry won the race
            db.rollback()
            event = _existing(db, user.id, body.idempotency_key)
    response.status_code = 200
    return event


@router.get("/progress/summary", response_model=ProgressSummary)
def progress_summary(user: User = Depends(current_user), db: Session = Depends(get_db)):
    rows = db.execute(
        select(ProgressEvent.type, func.count(), func.sum(ProgressEvent.xp_awarded))
        .where(ProgressEvent.user_id == user.id).group_by(ProgressEvent.type)
    ).all()
    return ProgressSummary(
        total_xp=sum(r[2] or 0 for r in rows),
        events=sum(r[1] for r in rows),
        by_type={r[0]: r[1] for r in rows},
    )
