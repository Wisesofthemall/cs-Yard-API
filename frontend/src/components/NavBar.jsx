import { NavLink, Link } from 'react-router-dom'

function NavBar() {
  const linkClass = ({ isActive }) =>
    'nav-link' + (isActive ? ' active fw-semibold' : '')

  return (
    <nav className="navbar navbar-expand-lg yard-navbar">
      <div className="container">
        <Link className="navbar-brand fw-bold" to="/">
          The Yard
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#yardNav"
          aria-controls="yardNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon" />
        </button>
        <div className="collapse navbar-collapse" id="yardNav">
          <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <NavLink className={linkClass} to="/" end>
                Home
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className={linkClass} to="/opportunities">
                Opportunities
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className={linkClass} to="/opportunities/new">
                Add New
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className={linkClass} to="/about">
                About
              </NavLink>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  )
}

export default NavBar
