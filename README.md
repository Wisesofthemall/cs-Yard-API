# The Yard API — HBCU Organizations & Opportunities Directory

A read-only REST API that helps HBCU students discover campus organizations, scholarship opportunities, professional development programs, and community events.

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

## Running the Tests

From the project root:

```bash
mvn test
```

## API Endpoints (Part 1 — Read Only)

All endpoints return JSON. Base URL: `http://localhost:8080`.

### Get all opportunities

Returns a JSON array of all opportunities. Optional query parameters:

- **`type`** — filter by opportunity type (case-insensitive): `Scholarship`, `Internship`, `Organization`, `Event`, `Fellowship`
- **`q`** — keyword search in title and tags (case-insensitive)

| Method | URL | Query params |
|--------|-----|--------------|
| GET | `http://localhost:8080/api/opportunities` | none |
| GET | `http://localhost:8080/api/opportunities` | `type=Scholarship` |
| GET | `http://localhost:8080/api/opportunities` | `q=STEM` |
| GET | `http://localhost:8080/api/opportunities` | `type=Internship`, `q=paid` |

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

| Method | URL | Response |
|--------|-----|----------|
| GET | `http://localhost:8080/api/opportunities/opp-001` | 200 OK with opportunity JSON |
| GET | `http://localhost:8080/api/opportunities/opp-999` | 404 Not Found |

**Sample Postman request — valid ID**

- Method: **GET**
- URL: `http://localhost:8080/api/opportunities/opp-001`

**Sample Postman request — invalid ID (404)**

- Method: **GET**
- URL: `http://localhost:8080/api/opportunities/opp-999`
- Expected: **404 Not Found**

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
