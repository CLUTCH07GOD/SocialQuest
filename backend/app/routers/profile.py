from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from ..db import get_db
from ..deps import current_user
from ..models import User
from ..schemas import ProfileOut, ProfilePatch

router = APIRouter(prefix="/profile", tags=["profile"])


@router.get("", response_model=ProfileOut)
def get_profile(user: User = Depends(current_user)):
    return user.profile


@router.patch("", response_model=ProfileOut)
def patch_profile(body: ProfilePatch, user: User = Depends(current_user), db: Session = Depends(get_db)):
    for field, value in body.model_dump(exclude_unset=True).items():
        if value is None and field not in ("display_name", "goal"):
            continue  # ignore nulls for non-nullable fields
        setattr(user.profile, field, value)
    db.commit()
    return user.profile
