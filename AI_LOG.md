# AI log — Yard API persistence (JPA / H2)

This log documents how AI tools were used for the database and persistence assignment, per course requirements.

## How AI was used

AI was **not** used to generate the full implementation. I used it mainly for **planning** and **light help**: turning the assignment rubric into a structured checklist, suggesting a reasonable table layout (e.g. separating tags into a related table), and occasional clarifications while I coded. **I wrote and own the code, configuration, tests, and documentation** in this repository.

## Planning phase

**Prompts (summary):** I asked for a step-by-step plan to satisfy the assignment: JPA + a relational DB, entities and repositories, wiring the existing endpoints to persistence, tests, README updates, and demo/AI-log deliverables.

**What the plan suggested (reference only):** Use Spring Data JPA and H2; model `opportunity` and tag storage with a foreign-key relationship; configure a datasource; replace the in-memory store in the service layer; add integration-style tests; document the schema in the README.

I **implemented** those ideas myself—file names, queries, edge cases, and refactors are my work unless noted below.

## Small assists during implementation

AI occasionally suggested patterns (for example, using empty strings instead of `NULL` in native filter parameters to avoid awkward SQL, or resetting test data between integration tests). I **evaluated** each suggestion, adapted it where it fit, and rejected or changed it when it did not match how I wanted the project to behave.

**Changes I made while building (examples):**

- Adjusted native SQL filter bindings so unused filters did not rely on `NULL` semantics I didn’t trust across the board.
- Fixed integration test isolation by re-seeding the database in `@BeforeEach` so generated ids did not collide across tests.
- Tightened assertions on filtered lists (e.g. every result matches the type filter) instead of assuming array position.

**Verification:** I ran `mvn test` locally and manually exercised the API (e.g. Postman) to confirm status codes, validation, and persistence matched the prior API contract.

## Demo video and README

I recorded the demo and added the video link to [`README.md`](README.md) myself.

## Summary

| Role of AI      | Planning outline, occasional hints while coding      |
| --------------- | ---------------------------------------------------- |
| Role of student | All implementation, testing, integration, docs, demo |
