# Java & Spring Boot guidelines

- Java 21; use records for immutable DTOs where appropriate.
- Spring Boot 3.x; constructor injection only; no field `@Autowired`.
- Package layout: `config`, `web`, `service`, `domain`, `persistence`.
- REST under `/api/v1`; use `@Valid` on request bodies; return problem-style JSON via `@ControllerAdvice`.
- Business rules (state machine) live in `service`/`domain`, not controllers.
- JPA: `Instant` for timestamps; `@PrePersist` / `@PreUpdate` for audit fields.
- Profiles: default H2 file store; `postgres` profile for production datasource from env.
- No secrets in code; use `application.yml` placeholders and `.env.example` only.
