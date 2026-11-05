import os

os.environ.setdefault("JWT_SECRET", "test-secret-0123456789-abcdef-0123456789")

import pytest
from fastapi.testclient import TestClient
from sqlalchemy.orm import sessionmaker
from sqlalchemy.pool import StaticPool

from app.db import Base, get_db, make_engine
from app.main import app
from app.providers import get_llm
from app.ratelimit import auth_limiter


class FakeLLM:
    async def reply(self, system, history, user_text):
        return f"echo: {user_text} (turns so far: {len(history) // 2})"


@pytest.fixture()
def db_maker():
    engine = make_engine("sqlite://", poolclass=StaticPool)
    Base.metadata.create_all(engine)
    return sessionmaker(bind=engine, expire_on_commit=False)


@pytest.fixture()
def client(db_maker):
    def _get_db():
        db = db_maker()
        try:
            yield db
        finally:
            db.close()

    app.dependency_overrides[get_db] = _get_db
    app.dependency_overrides[get_llm] = lambda: FakeLLM()
    auth_limiter.reset()
    auth_limiter.limit = 1000
    yield TestClient(app)
    app.dependency_overrides.clear()


REGISTER = {
    "email": "Sam@Example.com", "password": "correct-horse-1", "age_confirmed": True,
    "terms_version": "1.0", "privacy_version": "1.0", "nickname": "Sam",
}


def register(client, **over):
    return client.post("/api/v1/auth/register", json={**REGISTER, **over})


@pytest.fixture()
def auth(client):
    token = register(client).json()["access_token"]
    return {"Authorization": f"Bearer {token}"}


def other_auth(client):
    token = register(client, email="other@example.com").json()["access_token"]
    return {"Authorization": f"Bearer {token}"}
