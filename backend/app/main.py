from fastapi import APIRouter, FastAPI
from fastapi.middleware.cors import CORSMiddleware

from . import config
from .routers import auth, consents, me, profile, progress, sessions, ws

app = FastAPI(title="Social Quest API", version="0.1.0")

# The Android app does not need CORS. Set ALLOWED_ORIGINS only if a browser client is ever added.
if config.allowed_origins():
    app.add_middleware(CORSMiddleware, allow_origins=config.allowed_origins(), allow_methods=["*"], allow_headers=["*"])

v1 = APIRouter(prefix="/api/v1")
for r in (auth.router, me.router, consents.router, profile.router, sessions.router, progress.router):
    v1.include_router(r)


@v1.get("/health", tags=["meta"])
def health():
    return {"status": "ok"}


app.include_router(v1)
app.include_router(ws.router, prefix="/api/v1")
