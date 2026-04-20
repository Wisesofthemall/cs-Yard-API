import { Link } from 'react-router-dom'

function HomePage() {
  return (
    <>
      <section className="yard-hero text-center text-white py-5 mb-5">
        <div className="container py-4">
          <h1 className="display-4 fw-bold mb-3">
            Discover opportunities across The Yard
          </h1>
          <p className="lead mb-4">
            A single directory for HBCU scholarships, internships,
            fellowships, organizations, and events — so nothing gets lost
            in a group chat again.
          </p>
          <div className="d-flex justify-content-center gap-3 flex-wrap">
            <Link to="/opportunities" className="btn btn-light btn-lg px-4">
              View All Opportunities
            </Link>
            <Link
              to="/opportunities/new"
              className="btn btn-outline-light btn-lg px-4"
            >
              Add New
            </Link>
          </div>
        </div>
      </section>

      <section className="container mb-5">
        <h2 className="h3 mb-4 text-center">What can you do here?</h2>
        <div className="row g-4">
          <div className="col-md-4">
            <div className="card h-100 shadow-sm yard-feature-card">
              <div className="card-body">
                <h3 className="h5 card-title">Browse &amp; filter</h3>
                <p className="card-text text-muted">
                  Find opportunities by type (scholarship, internship,
                  fellowship, event, organization) or search by keyword
                  across titles and tags.
                </p>
              </div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card h-100 shadow-sm yard-feature-card">
              <div className="card-body">
                <h3 className="h5 card-title">Create &amp; edit</h3>
                <p className="card-text text-muted">
                  Advisors and student leaders can post new opportunities,
                  update deadlines, and keep descriptions current.
                </p>
              </div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card h-100 shadow-sm yard-feature-card">
              <div className="card-body">
                <h3 className="h5 card-title">Retire stale listings</h3>
                <p className="card-text text-muted">
                  Remove opportunities that have passed their deadline so
                  the board stays relevant and trustworthy.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  )
}

export default HomePage
