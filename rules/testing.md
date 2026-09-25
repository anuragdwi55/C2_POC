# Testing guidelines

- Prefer integration tests for state machine and HTTP contract.
- Use `@SpringBootTest(webEnvironment = RANDOM_PORT)` or MockMvc with full context for API tests.
- `@DataJpaTest` for repository queries (search/filter).
- Name tests: `shouldRejectWhenClosedToOpen`.
- Assert HTTP status and JSON `message` / `fieldErrors`, not only status code.
- Run `cd backend && ./mvnw test` before merge.
- Do not disable failing tests; fix or update spec first.
