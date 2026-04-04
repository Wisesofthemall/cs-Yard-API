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

## Demo video (assignment)

- **Demo:** [Yard API — database & JPA demo](https://youtu.be/nA0Sypbtrys)

## AI use

See [`AI_LOG.md`](AI_LOG.md) for prompts, outputs, and how results were reviewed or changed.

## AI Disclosure

This README was updated for the persistence assignment. AI assistance is documented in [`AI_LOG.md`](AI_LOG.md); earlier README drafting also used ChatGPT as noted in prior versions.
