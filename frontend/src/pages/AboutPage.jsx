import PageHeader from '../components/PageHeader.jsx'

function AboutPage() {
  return (
    <div className="container py-4">
      <PageHeader
        title="About The Yard API"
        subtitle="Project overview, tech stack, and how the pieces fit together."
      />

      <section className="mb-4">
        <h2 className="h4">What is this?</h2>
        <p>
          The Yard is a directory of HBCU organizations, scholarships,
          internships, fellowships, and events. This site (PA05) is the
          user-facing frontend for the REST API built in PA04. Everything
          you see — lists, detail views, forms, delete flows — talks to
          the same Spring Boot backend over HTTP.
        </p>
      </section>

      <section className="mb-4">
        <h2 className="h4">Frontend technologies</h2>
        <ul>
          <li>
            <strong>React 19</strong> with functional components and hooks
          </li>
          <li>
            <strong>Vite</strong> for development server and production
            bundling
          </li>
          <li>
            <strong>React Router 7</strong> for client-side navigation
          </li>
          <li>
            <strong>Bootstrap 5</strong> for responsive layout and UI
            primitives
          </li>
          <li>
            A separate <strong>custom CSS</strong> file for brand colors,
            typography, hero styling, and interactive states
          </li>
          <li>
            Native <strong>fetch API</strong> for HTTP calls (no extra
            client library)
          </li>
          <li>
            <strong>React Context</strong> for a shared alert system
          </li>
        </ul>
      </section>

      <section className="mb-4">
        <h2 className="h4">Backend technologies</h2>
        <ul>
          <li>
            <strong>Java 17</strong> and <strong>Spring Boot 3.2</strong>
          </li>
          <li>
            <strong>Spring Data JPA</strong> + <strong>H2</strong>{' '}
            (file-backed) for persistence
          </li>
          <li>
            <strong>Bean Validation</strong> for request body constraints
          </li>
          <li>
            A <code>@ControllerAdvice</code> that turns validation
            failures into a JSON map of field → message
          </li>
        </ul>
      </section>

      <section className="mb-4">
        <h2 className="h4">How the frontend talks to the API</h2>
        <p>
          The React app runs on <code>http://localhost:5173</code> during
          development. A <code>CorsConfig</code> bean on the Spring side
          whitelists that origin so browser requests aren&apos;t blocked
          by the same-origin policy. All API calls go through one service
          module (<code>src/services/opportunityApi.js</code>) that wraps{' '}
          <code>fetch</code> — it sets JSON headers, parses responses,
          and throws typed errors the pages can catch.
        </p>
        <p className="mb-0">
          When a form submission fails validation, the backend returns{' '}
          <code>400</code> with a <code>{'{field: message}'}</code> body.
          The form maps that object straight onto its local{' '}
          <code>fieldErrors</code> state and renders the error under each
          input — the same code path handles both client-side and
          server-side errors.
        </p>
      </section>

      <section className="mb-4">
        <h2 className="h4">API endpoints used</h2>
        <ul className="list-unstyled">
          <li>
            <code>GET /api/opportunities</code> — list with optional{' '}
            <code>?type=</code> and <code>?q=</code> filters
          </li>
          <li>
            <code>GET /api/opportunities/{'{id}'}</code> — single record
          </li>
          <li>
            <code>POST /api/opportunities</code> — create (server assigns
            id)
          </li>
          <li>
            <code>PUT /api/opportunities/{'{id}'}</code> — full update
          </li>
          <li>
            <code>DELETE /api/opportunities/{'{id}'}</code> — remove
          </li>
        </ul>
      </section>
    </div>
  )
}

export default AboutPage
