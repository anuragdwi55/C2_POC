# REST API contract

Base path: `/api/v1`

## Tickets

### POST `/tickets`

Request:

```json
{
  "title": "string",
  "description": "string",
  "priority": "LOW|MEDIUM|HIGH|CRITICAL",
  "assignee": "optional string"
}
```

Response `201`: ticket DTO with `status: "OPEN"`.

### GET `/tickets`

Query: `status` (optional), `q` (optional keyword), `page`, `size`.

Response `200`: `{ "content": [...], "page", "size", "totalElements" }`.

### GET `/tickets/{id}`

Response `200`: ticket with `comments[]`. `404` if missing.

### PATCH `/tickets/{id}`

Partial update: `title`, `description`, `priority`, `assignee` (all optional). Omitted fields are unchanged. Blank `title` or `description` when sent returns `400` with `fieldErrors`.

Response `200`: updated ticket.

### PATCH `/tickets/{id}/status`

Request:

```json
{ "status": "IN_PROGRESS" }
```

Response `200` on valid transition. `409` on invalid transition with message.

### POST `/tickets/{id}/comments`

Request:

```json
{
  "author": "string",
  "body": "string"
}
```

Response `201`: comment DTO.

## Error envelope

```json
{
  "timestamp": "ISO-8601",
  "status": 400,
  "error": "Bad Request",
  "message": "Human-readable summary",
  "fieldErrors": [
    { "field": "title", "message": "must not be blank" }
  ]
}
```

## CORS

Allow frontend origin in dev (`http://localhost:3000`).
