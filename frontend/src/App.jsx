import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import NavBar from './components/NavBar.jsx'
import Footer from './components/Footer.jsx'
import HomePage from './pages/HomePage.jsx'
import AboutPage from './pages/AboutPage.jsx'
import OpportunityListPage from './pages/OpportunityListPage.jsx'

function ComingSoon({ label }) {
  return (
    <div className="container py-5 text-center">
      <h1 className="h3 mb-3">{label}</h1>
      <p className="text-muted mb-4">
        This page is wired into the router shell and will be implemented
        in the next commits.
      </p>
      <Link to="/" className="btn btn-primary">
        Back to Home
      </Link>
    </div>
  )
}

function NotFound() {
  return (
    <div className="container py-5 text-center">
      <h1 className="display-5">404</h1>
      <p className="text-muted mb-4">That page isn&apos;t on The Yard.</p>
      <Link to="/" className="btn btn-primary">
        Back to Home
      </Link>
    </div>
  )
}

function App() {
  return (
    <BrowserRouter>
      <div className="yard-shell d-flex flex-column min-vh-100">
        <NavBar />
        <main className="flex-grow-1">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/about" element={<AboutPage />} />
            <Route path="/opportunities" element={<OpportunityListPage />} />
            <Route
              path="/opportunities/new"
              element={<ComingSoon label="Create form coming soon" />}
            />
            <Route
              path="/opportunities/:id"
              element={<ComingSoon label="Detail view coming soon" />}
            />
            <Route
              path="/opportunities/:id/edit"
              element={<ComingSoon label="Edit form coming soon" />}
            />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </BrowserRouter>
  )
}

export default App
