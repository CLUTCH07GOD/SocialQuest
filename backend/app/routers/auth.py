from datetime import datetime, timezone

from fastapi import APIRouter, Depends, HTTPException, Response
from sqlalchemy import select
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from ..db import get_db
from ..models import Profile, User
from ..ratelimit import auth_limiter
from ..schemas import LoginIn, RegisterIn, TokenOut
from ..security import create_token, hash_password, verify_password

router = APIRouter(prefix="/auth", tags=["auth"])


@router.post("/register", response_model=TokenOut, status_code=201, dependencies=[Depends(auth_limiter)])
def register(body: RegisterIn, db: Session = Depends(get_db)):
    # 18+ gate: without confirmation nothing is created or stored.
    if not body.age_confirmed:
        raise HTTPException(status_code=403, detail="age_not_confirmed")
    email = body.email.lower()
    if db.scalar(select(User).where(User.email == email)):
        raise HTTPException(status_code=409, detail="email_taken")
    user = User(
        email=email,
        password_hash=hash_password(body.password),
        age_confirmed_at=datetime.now(timezone.utc),
        terms_version=body.terms_version,
        privacy_version=body.privacy_version,
        profile=Profile(display_name=body.nickname or None),
    )
    db.add(user)
    try:
        db.commit()
    except IntegrityError:
        db.rollback()
        raise HTTPException(status_code=409, detail="email_taken")
    return TokenOut(access_token=create_token(user.id))


@router.post("/login", response_model=TokenOut, dependencies=[Depends(auth_limiter)])
def login(body: LoginIn, db: Session = Depends(get_db)):
    user = db.scalar(select(User).where(User.email == body.email.lower()))
    if not verify_password(user.password_hash if user else None, body.password):
        raise HTTPException(status_code=401, detail="invalid_credentials")
    return TokenOut(access_token=create_token(user.id))


@router.post("/logout", status_code=204)
def logout():
    # Tokens are stateless: the app deletes its stored token. (Server-side revocation can be added later.)
    return Response(status_code=204)
