# API standards

- Version prefix: `/api/v1`.
- JSON only; UTF-8; camelCase properties.
- Pagination: `page` (0-based), `size` (default 20, max 100).
- Errors: consistent envelope with `message`, optional `fieldErrors[]`.
- Idempotency: PATCH for partial updates; dedicated endpoint for status transitions.
- Use appropriate status codes: 201 create, 404 not found, 409 conflict (invalid transition), 400 validation.
- Document changes in `spec/api-contract.md` before implementation.
