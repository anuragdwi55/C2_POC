# Prompt history

Recorded prompts for spec-driven development (see also `.specstory/history/`).

## 2026-09-25 — Exercise kickoff

**Prompt (summary):** Build Support Ticket Management System with spec-driven workflow (Java 21, Spring Boot, H2/PostgreSQL, Next.js), hygiene files (rules, skills, commands), spec artefacts, prompt history, and document AI mistakes.

**Outcome:** Created spec set before implementation; avoided "build complete application" in one shot.

---

## AI review log (mistakes / rejected suggestions)

Engineering review of AI output — do not accept blindly.

| # | AI suggestion | Why wrong / rejected | Correct approach |
|---|---------------|----------------------|------------------|
| 1 | Use `@Enumerated(EnumType.ORDINAL)` for status | Ordinal breaks when enum order changes; risky for migrations | `@Enumerated(EnumType.STRING)` per JPA best practice |
| 2 | Allow status change via generic PATCH on `status` field | Hides transition rules; easy to bypass state machine | Dedicated `PATCH /tickets/{id}/status` enforced in service |
| 3 | Store comments as JSON column on ticket | Harder to query; violates normalized model in spec | Separate `Comment` entity with FK |
| 4 | Frontend: enable all status dropdown values | Users could request illegal transitions | Show only legal next states from current status |
| 5 | Use in-memory H2 without file URL | Data lost on restart; fails NFR-06 | `jdbc:h2:file:./data/tickets` for dev |

*(Add new rows as you iterate with Copilot/Cursor.)*
