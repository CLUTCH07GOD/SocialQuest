import time
from collections import defaultdict, deque

from fastapi import HTTPException, Request


class RateLimiter:
    """Small in-memory limiter per client address and path. Fine for one server; use Redis if you ever scale out."""

    def __init__(self, limit: int = 10, window: int = 60):
        self.limit, self.window = limit, window
        self.hits: dict[str, deque] = defaultdict(deque)

    def reset(self) -> None:
        self.hits.clear()

    def __call__(self, request: Request) -> None:
        key = f"{request.client.host if request.client else 'unknown'}:{request.url.path}"
        now, q = time.monotonic(), self.hits[key]
        while q and now - q[0] > self.window:
            q.popleft()
        if len(q) >= self.limit:
            raise HTTPException(status_code=429, detail="too_many_requests")
        q.append(now)


auth_limiter = RateLimiter(limit=10, window=60)
