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

---

# AI log — PA05 React frontend

This section documents AI use while building the React frontend on top of the existing PA04 API.

## Tool used

Claude Code (Claude Opus 4.7) running in the terminal inside VS Code.

## Planning phase

**Prompt (summary):** "Create a plan to build the complete PA05 React frontend for this Yard API backend — all required pages, components, Bootstrap 5 + custom CSS, Router, Context, fetch, and meaningful commits on a `pa05-frontend` branch."

**Output (summary):** A structured plan covering (a) adding CORS to the backend (blocker — no config existed), (b) scaffolding a Vite + React app under `frontend/`, (c) a file layout with `components/ pages/ context/ services/ styles/`, (d) a shared `OpportunityForm` used by both Create and Edit, (e) an `AppContext` scoped to alerts only, and (f) an eight-commit sequence ending with custom CSS polish and docs.

**What I changed vs. the suggestion:**

- Initially considered a chip/pill input for the `tags` field. I rejected that — the assignment rewards controlled forms, not input sophistication — and went with a comma-separated text input that splits on submit.
- The plan suggested caching the opportunity list in `AppContext`. I rejected this. Caching requires invalidation after every mutation, which is exactly the class of bug the assignment is trying to avoid. Context holds only alerts; list/detail pages refetch on mount.
- The plan suggested using `@CrossOrigin` per-controller as a quick option. I used a `WebMvcConfigurer` bean instead so CORS rules live in one file and survive future controller additions.
- The plan suggested clearing alerts on route change. I removed that — it would clear success alerts set by Create/Edit/Delete *before* the redirected page rendered. Success alerts auto-dismiss after 5s; danger alerts stay until dismissed.

## Small assists during implementation

- Asked for a Bootstrap 5 modal pattern that works without importing the Bootstrap JS Modal class. Used a state-driven approach with `display: block` + manual backdrop + ESC handler.
- Asked for a concise CSS pattern for design tokens. Used CSS custom properties on `:root` so component styles reference `var(--yard-primary)` instead of hardcoded hex.
- Asked about `:focus-visible` vs `:focus`. Used `:focus-visible` so the focus ring only appears for keyboard navigation.
- Asked about mapping Spring validation errors onto a React form. Confirmed the existing `ValidationExceptionHandler` already returns `{field: message}`, so the form assigns `err.body` directly to `fieldErrors` state — no normalization layer needed.

**Changes I made while building (examples):**

- Simplified the "flash message after redirect" pattern from `navigate(path, { state: { flash } })` to calling `showAlert` *before* `navigate`. Since context is above the router, the alert survives the route change and the receiving page does not need flash-reading logic.
- Removed the `ComingSoon` placeholder component from `App.jsx` once all pages were wired up — no backwards-compat shim needed.
- Added URL validation (`new URL(...)` + protocol check) on the client so users see "enter a valid URL" without a server roundtrip, while still letting the backend be the source of truth on 400.
- Added `AbortController` to list/detail/edit fetches so switching routes mid-request cancels the in-flight call instead of setting state on an unmounted component.

**Verification:** Ran `mvn spring-boot:run`, `cd frontend && npm run dev`, manually exercised every page against the live H2-backed API: golden-path create → view → edit → delete, plus error paths (empty form, invalid URL, missing ID). Also verified CORS end-to-end with `curl -H "Origin: http://localhost:5173" -i http://localhost:8080/api/opportunities`.

## Demo video and README

I recorded the PA05 demo and added the link to [`README.md`](README.md) myself.

## Summary (PA05)

| Role of AI      | Planning outline, Bootstrap/CSS pattern suggestions, validation pattern confirmation |
| --------------- | ------------------------------------------------------------------------------------ |
| Role of student | All React code, styling choices, form logic, CORS config, docs, demo                 |
