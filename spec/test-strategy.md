# Test strategy

## Backend

| Area | Type | Location |
|------|------|----------|
| Status transitions (all allowed + invalid samples) | `@SpringBootTest` + MockMvc or TestRestTemplate | `StatusTransitionIntegrationTest` |
| Validation on create/update | MockMvc | `TicketControllerTest` |
| Repository search/filter | `@DataJpaTest` | `TicketRepositoryTest` |
| Service unit | JUnit | `StatusTransitionServiceTest` |

## Frontend

- Manual checklist against acceptance criteria (no E2E harness required for POC)
- Optional: component tests for error mapping

## CI command

```bash
cd backend && ./mvnw -q test
cd frontend && npm test --if-present
```

## Traceability

- FR-05 / state machine → `StatusTransitionIntegrationTest`
- FR-09 persistence → H2 file DB + restart test optional (integration uses same DB config)
- FR-10 → validation tests on controller
