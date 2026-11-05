# Social Quest

Android app (Kotlin + Jetpack Compose) with a FastAPI + PostgreSQL backend. Adults 18+ only.

- `android/`  the app. See `android/README.md` for setup.
- `backend/`  API, schema and migrations (Person 1 owns migrations).
- `docs/`     `api-contracts.md` (read this first), `openapi.json`.

## Run the backend (no Docker, SQLite)
```bash
cd backend
python -m venv venv && source venv/bin/activate      # Windows: venv\Scripts\activate
pip install -r requirements-dev.txt
cp .env.example .env     # then put a random JWT_SECRET in it (32+ chars)
alembic upgrade head
uvicorn app.main:app --reload --env-file .env
pytest -q
```
Open http://127.0.0.1:8000/docs. The emulator reaches it at http://10.0.2.2:8000.

## Run with Docker (Postgres)
Copy `.env.example` to `.env`, fill in `POSTGRES_PASSWORD` and `JWT_SECRET`, then `docker compose up --build`.

## Rules
Never commit `.env`, keys or keystores. Only P1 writes migrations. Branches + pull requests; CI must pass.
