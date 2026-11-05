from fastapi import APIRouter, Depends, Response
from sqlalchemy.orm import Session

from ..db import get_db
from ..deps import current_user
from ..models import User
from ..schemas import MeOut

router = APIRouter(prefix="/me", tags=["me"])


@router.get("", response_model=MeOut)
def me(user: User = Depends(current_user)):
    return user


@router.delete("", status_code=204)
def delete_me(user: User = Depends(current_user), db: Session = Depends(get_db)):
    """Full deletion. Every user-owned table cascades."""
    db.delete(user)
    db.commit()
    return Response(status_code=204)


@router.get("/export")
def export_me(user: User = Depends(current_user)):
    p = user.profile
    return {
        "user": {
            "id": user.id, "email": user.email, "created_at": user.created_at,
            "age_confirmed_at": user.age_confirmed_at,
            "terms_version": user.terms_version, "privacy_version": user.privacy_version,
        },
        "profile": {
            "display_name": p.display_name, "situations": p.situations, "difficulties": p.difficulties,
            "goal": p.goal, "preferred_mode": p.preferred_mode, "captions": p.captions,
            "reduce_motion": p.reduce_motion, "keep_summary": p.keep_summary,
        } if p else None,
        "consents": [{"type": c.type, "version": c.version, "accepted_at": c.accepted_at} for c in user.consents],
        "sessions": [
            {"id": s.id, "situation_id": s.situation_id, "mode": s.mode, "status": s.status,
             "started_at": s.started_at, "ended_at": s.ended_at}
            for s in user.sessions
        ],
        "progress_events": [
            {"type": e.type, "ref": e.ref, "xp_awarded": e.xp_awarded, "created_at": e.created_at}
            for e in user.progress_events
        ],
    }
