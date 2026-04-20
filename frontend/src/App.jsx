import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import NavBar from './components/NavBar.jsx'
import Footer from './components/Footer.jsx'
import AlertMessage from './components/AlertMessage.jsx'
import HomePage from './pages/HomePage.jsx'
import AboutPage from './pages/AboutPage.jsx'
import OpportunityListPage from './pages/OpportunityListPage.jsx'
import OpportunityDetailPage from './pages/OpportunityDetailPage.jsx'
import OpportunityCreatePage from './pages/OpportunityCreatePage.jsx'
import OpportunityEditPage from './pages/OpportunityEditPage.jsx'
import { AppContextProvider } from './context/AppContext.jsx'

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
      <AppContextProvider>
        <div className="yard-shell d-flex flex-column min-vh-100">
          <NavBar />
          <AlertMessage />
          <main className="flex-grow-1">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/about" element={<AboutPage />} />
              <Route
                path="/opportunities"
                element={<OpportunityListPage />}
              />
              <Route
                path="/opportunities/new"
                element={<OpportunityCreatePage />}
              />
              <Route
                path="/opportunities/:id"
                element={<OpportunityDetailPage />}
              />
              <Route
                path="/opportunities/:id/edit"
                element={<OpportunityEditPage />}
              />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </AppContextProvider>
    </BrowserRouter>
  )
}

export default App
