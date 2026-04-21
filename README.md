# The Yard API — HBCU Organizations & Opportunities Directory

A REST API that helps HBCU students discover campus organizations, scholarship opportunities, professional development programs, and community events. **Part 1** provides read-only access (GET). **Part 2** adds write operations (POST, PUT, DELETE) so advisors and student organization leaders can manage the opportunities directory programmatically — adding listings, correcting outdated information, and removing opportunities that have passed their deadline. The API supports the full set of HTTP methods used in real-world REST services: GET, POST, PUT, and DELETE.

## Prerequisites

- **JDK 17 or later**
- **Maven 3.6+** (or use the Maven wrapper if present)

## Running the Application

1. Clone the repository and open a terminal in the project root.
2. Start the application:

   ```bash
   mvn spring-boot:run
   ```

3. The API will be available at `http://localhost:8080`.

### Database (Part 3 — persistence)

Opportunities are stored in **H2** using **Spring Data JPA**. By default the app uses a **file-backed** database under `./data/` (see [`application.properties`](src/main/resources/application.properties)) so data survives restarts—useful for demos and local development. The `./data/` directory is gitignored.

- **H2 console** (dev only): with the app running, open `http://localhost:8080/h2-console`. JDBC URL must match your config (e.g. `jdbc:h2:file:./data/yard`), user `sa`, empty password unless you change it.

#### Schema

| Table                 | Description                                                                                                                                                     |
| --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`opportunity`**     | One row per listing. **Primary key:** `id` (string, e.g. `opp-001`). Columns: `title`, `type`, `sponsor`, `deadline`, `description`, `url`.                     |
| **`opportunity_tag`** | One row per tag; **foreign key** `opportunity_id` → `opportunity.id`. Storing tags in a child table matches the API’s `tags` array and supports keyword search. |

Hibernate creates/updates tables (`spring.jpa.hibernate.ddl-auto=update`). On first run with an empty database, [`OpportunityDataSeeder`](src/main/java/edu/famu/cop3060/yard/config/OpportunityDataSeeder.java) loads eight sample opportunities (`opp-001` … `opp-008`). New creates get the next `opp-NNN` id based on the **maximum numeric suffix** in the database.

#### Endpoint summary

| Method | Path                      | Purpose                        |
| ------ | ------------------------- | ------------------------------ |
| GET    | `/api/opportunities`      | List all; optional `type`, `q` |
| GET    | `/api/opportunities/{id}` | Get one                        |
| POST   | `/api/opportunities`      | Create (server-generated `id`) |
| PUT    | `/api/opportunities/{id}` | Full replace                   |
| DELETE | `/api/opportunities/{id}` | Delete                         |

## Running the Tests

From the project root:

```bash
mvn test
```

If you use **Java 23 or later**, the project enables Byte Buddy’s experimental support so Mockito-based tests run correctly (no extra setup needed).

Controller slice tests live in `OpportunitiesControllerTest`; full-stack persistence tests are in `OpportunitiesApiIntegrationTest` (in-memory H2, database reset per test).

## API Endpoints (Part 1 — Read Only)

All endpoints return JSON. Base URL: `http://localhost:8080`.

### Get all opportunities

Returns a JSON array of all opportunities. Optional query parameters:

- **`type`** — filter by opportunity type (case-insensitive): `Scholarship`, `Internship`, `Organization`, `Event`, `Fellowship`
- **`q`** — keyword search in title and tags (case-insensitive)

| Method | URL                                       | Query params                |
| ------ | ----------------------------------------- | --------------------------- |
| GET    | `http://localhost:8080/api/opportunities` | none                        |
| GET    | `http://localhost:8080/api/opportunities` | `type=Scholarship`          |
| GET    | `http://localhost:8080/api/opportunities` | `q=STEM`                    |
| GET    | `http://localhost:8080/api/opportunities` | `type=Internship`, `q=paid` |

**Sample Postman request — all opportunities**

- Method: **GET**
- URL: `http://localhost:8080/api/opportunities`

**Sample Postman request — filter by type**

- Method: **GET**
- URL: `http://localhost:8080/api/opportunities`
- Query params: `type` = `Scholarship`

**Sample Postman request — search by keyword**

- Method: **GET**
- URL: `http://localhost:8080/api/opportunities`
- Query params: `q` = `STEM`

**Sample Postman request — combine filters**

- Method: **GET**
- URL: `http://localhost:8080/api/opportunities`
- Query params: `type` = `Internship`, `q` = `paid`

### Get a single opportunity by ID

| Method | URL                                               | Response                     |
| ------ | ------------------------------------------------- | ---------------------------- |
| GET    | `http://localhost:8080/api/opportunities/opp-001` | 200 OK with opportunity JSON |
| GET    | `http://localhost:8080/api/opportunities/opp-999` | 404 Not Found                |

**Sample Postman request — valid ID**

- Method: **GET**
- URL: `http://localhost:8080/api/opportunities/opp-001`

**Sample Postman request — invalid ID (404)**

- Method: **GET**
- URL: `http://localhost:8080/api/opportunities/opp-999`
- Expected: **404 Not Found**

## API Endpoints (Part 2 — Write Operations)

Base URL: `http://localhost:8080`. All write requests that include a body must use `Content-Type: application/json`.

### Create a new opportunity (POST)

Creates a new opportunity listing. The server generates a unique ID; do not send `id` in the body. On success, the response is **201 Created** with the full opportunity (including the generated ID) in the body and a **Location** header pointing to the new resource.

| Method | URL                                       | Response                                       |
| ------ | ----------------------------------------- | ---------------------------------------------- |
| POST   | `http://localhost:8080/api/opportunities` | 201 Created with body; 400 if validation fails |

**Sample Postman request — create (POST)**

- Method: **POST**
- URL: `http://localhost:8080/api/opportunities`
- Header: **Content-Type** = `application/json`
- Body (raw JSON):

```json
{
  "title": "New HBCU Leadership Scholarship",
  "type": "Scholarship",
  "sponsor": "Example Foundation",
  "deadline": "2025-08-01",
  "description": "Merit-based scholarship for leadership and community service.",
  "tags": ["leadership", "undergrad", "community"],
  "url": "https://example.com/leadership-scholarship"
}
```

### Update an existing opportunity (PUT)

Replaces an existing opportunity in full. The `id` is in the URL path; the body contains all required fields (same shape as create). **200 OK** returns the updated opportunity; **404 Not Found** if the ID does not exist (this endpoint does not create new records).

| Method | URL                                               | Response                            |
| ------ | ------------------------------------------------- | ----------------------------------- |
| PUT    | `http://localhost:8080/api/opportunities/opp-001` | 200 OK with body; 404 if ID missing |

**Sample Postman request — update (PUT)**

- Method: **PUT**
- URL: `http://localhost:8080/api/opportunities/opp-001`
- Header: **Content-Type** = `application/json`
- Body (raw JSON):

```json
{
  "title": "UNCF STEM Scholarship (Updated)",
  "type": "Scholarship",
  "sponsor": "UNCF",
  "deadline": "2025-05-01",
  "description": "Updated description for STEM majors at HBCUs.",
  "tags": ["STEM", "undergrad", "paid"],
  "url": "https://uncf.org/programs/uncf-stem-scholarship"
}
```

### Delete an opportunity (DELETE)

Removes an opportunity permanently. **204 No Content** with no body on success; **404 Not Found** if the ID does not exist.

| Method | URL                                               | Response                          |
| ------ | ------------------------------------------------- | --------------------------------- |
| DELETE | `http://localhost:8080/api/opportunities/opp-001` | 204 No Content; 404 if ID missing |

**Sample Postman request — delete (DELETE)**

- Method: **DELETE**
- URL: `http://localhost:8080/api/opportunities/opp-001`
- No request body required.

### Verifying error responses (Postman)

- **PUT 404:** Use PUT with URL `http://localhost:8080/api/opportunities/opp-999` and a valid JSON body. Expected: **404 Not Found** and a WARN log in the terminal.
- **DELETE 404:** Use DELETE with URL `http://localhost:8080/api/opportunities/opp-999`. Expected: **404 Not Found** and a WARN log in the terminal.
- **POST 400:** Use POST to `http://localhost:8080/api/opportunities` with a JSON body that omits one or more required fields (e.g. omit `title`). Expected: **400 Bad Request** with a JSON body listing field-level validation errors.

## Sample request URL demonstrating filtering

Example that combines type and keyword filters:

```
http://localhost:8080/api/opportunities?type=Scholarship&q=STEM
```

## Opportunity response shape

Each opportunity object includes:

- `id` — unique identifier
- `title` — name of the opportunity
- `type` — one of: Scholarship, Internship, Organization, Event, Fellowship
- `sponsor` — organization or entity offering it
- `deadline` — application or RSVP deadline (may be empty for events/organizations)
- `description` — brief summary
- `tags` — array of keywords for searching
- `url` — link to learn more or apply

Walkthrough of the project:
[Walkthrough](https://youtu.be/jApyJaCWods?si=XRQhpMVOCE5jkVDa)

## Frontend (PA05)

PA05 adds a React single-page app in [`frontend/`](frontend/) that consumes every endpoint listed above. The backend and frontend run side-by-side during development.

### Frontend technologies

- **React 19** with functional components and hooks
- **Vite 8** for the dev server and production bundling
- **React Router 7** for routing between pages
- **Bootstrap 5.3** for responsive layout, navbar, cards, forms, alerts, modal
- A separate custom stylesheet at [`frontend/src/styles/custom.css`](frontend/src/styles/custom.css)
- Native **fetch API** wrapped in a single service module
- **React Context** (`AppContext`) for a shared alert system

### Backend technologies

- Java 17, Spring Boot 3.2, Spring Web MVC, Spring Data JPA
- H2 (file-backed) for persistence
- Bean Validation + a `@ControllerAdvice` that returns 400 with a `{field: message}` JSON map
- [`CorsConfig`](src/main/java/edu/famu/cop3060/yard/config/CorsConfig.java) whitelists `http://localhost:5173` for `/api/**` so the Vite dev server can reach the API

### Running the backend

```bash
mvn spring-boot:run
```

API at `http://localhost:8080`.

### Running the frontend

```bash
cd frontend
npm install
npm run dev
```

Dev server at `http://localhost:5173`. Start the backend first; the React app will fail to load data if port 8080 is not responding.

### Environment variables (optional)

Create `frontend/.env.local` to override the API base URL:

```
VITE_API_BASE_URL=http://localhost:8080/api
```

### Frontend pages

| Route                         | Page                    | Purpose                                                                |
| ----------------------------- | ----------------------- | ---------------------------------------------------------------------- |
| `/`                           | Home                    | Hero + feature cards + CTAs to browse and add                          |
| `/opportunities`              | List                    | All opportunities as cards; filter by type and keyword                 |
| `/opportunities/:id`          | Detail                  | Full record + Back, Edit, Delete actions                               |
| `/opportunities/new`          | Create                  | Controlled form, client-side + backend validation                      |
| `/opportunities/:id/edit`     | Edit                    | Same form pre-populated with the current record                        |
| `/about`                      | About / API info        | Tech stack + explanation of how the frontend talks to the API          |
| `*`                           | 404                     | Not-found fallback                                                     |

### API endpoints used by the frontend

| Method | Path                              | When                               |
| ------ | --------------------------------- | ---------------------------------- |
| GET    | `/api/opportunities?type=&q=`     | List page (with filters)           |
| GET    | `/api/opportunities/{id}`         | Detail + Edit pages on load        |
| POST   | `/api/opportunities`              | Create page on submit              |
| PUT    | `/api/opportunities/{id}`         | Edit page on submit                |
| DELETE | `/api/opportunities/{id}`         | Detail page delete confirmation    |

### CSS design choices

- **Design tokens** — custom palette, radii, shadows, and fonts are defined once as CSS variables on `:root` in `custom.css` so a color change is one edit, not a grep-and-replace.
- **Brand palette** — deep navy primary (`#12356f`) for trust, amber accent (`#f5a524`) for emphasis and active states — non-Bootstrap colors that give the app its own identity.
- **Typography** — Poppins for display headings, Inter for body; tighter letter-spacing on headings; larger `.lead` size for hero subcopy.
- **Hero section** — gradient background, soft radial highlight, rounded bottom corners, and white CTAs that invert on hover.
- **Interactive states** — cards lift on hover (`translateY(-2px)` + shadow), buttons darken on hover, and a visible amber focus ring (`:focus-visible`, 3px outline) keeps keyboard users oriented without adding noise for mouse users.
- **Navbar pills** — active nav item rendered as an amber pill for quick orientation between pages.
- **Responsive** — mobile media query collapses the hero radius and scales page-header headlines down on small screens.

### React concepts demonstrated

- **Components** — NavBar, Footer, PageHeader, AlertMessage, OpportunityCard, OpportunityForm, ConfirmDeleteModal (seven reusable components).
- **Props** — OpportunityCard receives an `opportunity` and optional `onDelete`; OpportunityForm takes `initialData`, `onSubmit`, `submitLabel`, `cancelHref`; PageHeader takes `title`, `subtitle`, `actions`.
- **State** — forms hold `formData`, `fieldErrors`, `submitting`; list/detail/edit pages track `loading`, `error`, and the fetched record.
- **Routing** — six real routes plus a wildcard 404, all served by `BrowserRouter`. The list page reads/writes its filters to URL query params so back/forward and shared links work.
- **Context** — `AppContext` exposes `alert`, `showAlert(type, message)`, and `clearAlert`; the Create, Edit, and Delete flows announce success through the context so the alert survives the redirect.
- **Forms** — every input is a controlled component; required fields are marked; labels are associated with inputs via `htmlFor`/`id`; submission is blocked until client-side checks pass; backend 400 errors are mapped directly onto the same `fieldErrors` state so client-side and server-side validation render the same way.

### Demo video (PA05)

- **PA05 demo:** _[link to be added after recording]_

## Demo video (assignment)

- **Demo:** [Yard API — database & JPA demo](https://youtu.be/nA0Sypbtrys)

## AI use

See [`AI_LOG.md`](AI_LOG.md) for prompts, outputs, and how results were reviewed or changed.

## AI Disclosure

This README was updated for the persistence assignment. AI assistance is documented in [`AI_LOG.md`](AI_LOG.md); earlier README drafting also used ChatGPT as noted in prior versions.
