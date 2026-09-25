# Architecture

## Overview

```
┌─────────────┐     REST/JSON      ┌──────────────────┐
│  Next.js    │ ◄────────────────► │  Spring Boot     │
│  (frontend) │                    │  (backend)       │
└─────────────┘                    └────────┬─────────┘
                                            │
                                    ┌───────▼────────┐
                                    │ H2 / PostgreSQL│
                                    └────────────────┘
```

## Backend layers

- **web**: REST controllers, validation, exception handler
- **service**: TicketService, StatusTransitionService
- **domain**: enums, transition rules
- **persistence**: JPA entities, repositories

## Frontend

- App Router (Next.js 14+)
- Server components for layout; client components for forms/lists
- Central `api` module; map `fieldErrors` to form fields

## Profiles

- `default` / `dev`: H2 file-based (`./data/tickets`) for restart persistence
- `postgres`: JDBC from env (`SPRING_DATASOURCE_*`)

## Spec-driven workflow

Requirements → this spec set → implementation → tests in `backend/src/test` → review via `commands/review-code.md`.
