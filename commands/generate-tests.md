# Command: Generate tests

```
Using spec/test-strategy.md and spec/state-machine.md, generate or extend tests only.
Cover every allowed transition and at least: CLOSED→OPEN, RESOLVED→OPEN, CANCELLED→OPEN, OPEN→CLOSED.
Use existing test style in backend/src/test/java.
Do not change production code unless a test reveals a bug—then fix minimally.
```

Run: `cd backend && ./mvnw test`
