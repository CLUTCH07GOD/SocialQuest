"""XP is for effort, never for performance, and is never negative. The server decides the amount, not the client."""
XP_RULES: dict[str, int] = {
    "session_started": 5,
    "session_completed": 20,
    "reflection_completed": 15,
    "ladder_step_tried": 10,
    "quest_completed": 20,
    "returned_after_difficult_attempt": 15,
}
