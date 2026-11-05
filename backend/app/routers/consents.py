from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from ..db import get_db
from ..deps import current_user
from ..models import Consent, User
from ..schemas import ConsentIn, ConsentOut

router = APIRouter(prefix="/consents", tags=["consents"])


@router.get("", response_model=list[ConsentOut])
def list_consents(user: User = Depends(current_user)):
    return user.consents


@router.post("", response_model=ConsentOut, status_code=201)
def accept_consent(body: ConsentIn, user: User = Depends(current_user), db: Session = Depends(get_db)):
    consent = Consent(user_id=user.id, type=body.type, version=body.version)
    db.add(consent)
    db.commit()
    return consent
