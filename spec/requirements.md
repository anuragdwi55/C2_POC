# Requirements — Support Ticket Management System

## Functional

| ID | Requirement | Priority |
|----|-------------|----------|
| FR-01 | Create a ticket (title, description, priority; default status OPEN) | Must |
| FR-02 | List tickets with pagination optional | Must |
| FR-03 | View ticket details including comments | Must |
| FR-04 | Update title, description, priority, assignee | Must |
| FR-05 | Transition status per state machine | Must |
| FR-06 | Add comments to a ticket | Must |
| FR-07 | Search tickets by keyword (title/description) | Must |
| FR-08 | Filter tickets by status | Must |
| FR-09 | Persist all data in a database | Must |
| FR-10 | Backend input validation with structured errors | Must |
| FR-11 | UI displays API validation and business errors | Must |

## Non-functional

| ID | Requirement |
|----|-------------|
| NFR-01 | Java 21, Spring Boot 3.x |
| NFR-02 | H2 for dev/test; PostgreSQL profile for production |
| NFR-03 | REST API, OpenAPI-friendly DTOs |
| NFR-04 | React/Next.js frontend |
| NFR-05 | No secrets in repository |
| NFR-06 | Data survives application restart |

## Out of scope

- Authentication / multi-tenant RBAC
- Email notifications
- File attachments

## Acceptance mapping

See `test-strategy.md` for traceability to automated tests.
