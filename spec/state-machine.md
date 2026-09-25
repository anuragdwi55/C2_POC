# Ticket status state machine

## States

- `OPEN`
- `IN_PROGRESS`
- `RESOLVED`
- `CLOSED`
- `CANCELLED` (terminal)

## Allowed transitions

| From | To |
|------|-----|
| OPEN | IN_PROGRESS, CANCELLED |
| IN_PROGRESS | RESOLVED, CANCELLED |
| RESOLVED | CLOSED |
| CLOSED | *(none)* |
| CANCELLED | *(none)* |

## Primary happy path

```
OPEN → IN_PROGRESS → RESOLVED → CLOSED
```

## Cancellation paths

```
OPEN → CANCELLED
IN_PROGRESS → CANCELLED
```

## Rejected examples (must return 409 or 422 with clear message)

- CLOSED → OPEN
- RESOLVED → OPEN
- CANCELLED → OPEN
- OPEN → CLOSED (skip steps)
- RESOLVED → CANCELLED

## Implementation notes

- Enforce in domain service, not only in controller.
- Integration tests must cover every allowed edge and sample invalid transitions.
