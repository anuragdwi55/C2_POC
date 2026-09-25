# Support Ticket Management System

Spec-driven POC built with **Java 21**, **Spring Boot 3**, **H2/PostgreSQL**, **Next.js**, and **Cursor** hygiene artefacts (`spec/`, `rules/`, `commands/`, `skills/`).

## Workflow

Requirement → `spec/` → `spec/plan-tasks.md` → implementation → tests → review (`commands/review-code.md`)

Prompt history: `docs/prompt-history.md`, `.specstory/history/`

## Run locally

### Backend (port 8080)

Requires **JDK 21** (`java -version`). Tests were run with Temurin 21.

```bash
cd backend
./mvnw spring-boot:run
```

Uses file-backed H2 at `backend/data/` so data survives restarts.

PostgreSQL profile:

```bash
export SPRING_PROFILES_ACTIVE=postgres
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tickets
export SPRING_DATASOURCE_USERNAME=tickets
export SPRING_DATASOURCE_PASSWORD=your_password
./mvnw spring-boot:run
```

### Frontend (port 3000)

```bash
cd frontend
cp .env.example .env.local
npm install
npm run dev
```

Set `NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1` if needed.

### Tests

```bash
cd backend && ./mvnw test
```

State machine coverage: `StatusTransitionIntegrationTest`.

## API

See `spec/api-contract.md`. Base path: `/api/v1/tickets`.

## AI-assisted development

See **AI review log** in `docs/prompt-history.md` for rejected AI suggestions (state machine via PATCH, enum ordinals, etc.).

## Token optimisation (optional)

For larger teams, consider MCP tools such as **Graphify**, **Caveman**, or **Codebase-memory** to shrink context sent to the model; this repo keeps specs in `spec/` as the source of truth to reduce redundant prompts.
