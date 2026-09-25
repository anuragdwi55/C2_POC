# Data model

## Entity: Ticket

| Field | Type | Constraints |
|-------|------|-------------|
| id | UUID | PK |
| title | string | not blank, max 200 |
| description | string | not blank, max 5000 |
| priority | enum | LOW, MEDIUM, HIGH, CRITICAL |
| status | enum | see state-machine.md |
| assignee | string | optional, max 100 |
| createdAt | instant | auto |
| updatedAt | instant | auto |

## Entity: Comment

| Field | Type | Constraints |
|-------|------|-------------|
| id | UUID | PK |
| ticketId | UUID | FK → Ticket |
| author | string | not blank, max 100 |
| body | string | not blank, max 2000 |
| createdAt | instant | auto |

## Relationships

- One Ticket → many Comments (cascade delete comments with ticket optional; keep ticket on comment delete)

## Indexes

- `ticket.status` for filter
- Full-text or `LIKE` search on title + description for keyword search (H2: LIKE for portability)
