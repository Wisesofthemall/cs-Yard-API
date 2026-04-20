import { useEffect, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import PageHeader from '../components/PageHeader.jsx'
import OpportunityCard from '../components/OpportunityCard.jsx'
import {
  listOpportunities,
  OPPORTUNITY_TYPES,
} from '../services/opportunityApi.js'

function OpportunityListPage() {
  const [searchParams, setSearchParams] = useSearchParams()
  const typeFilter = searchParams.get('type') || ''
  const qFilter = searchParams.get('q') || ''

  const [searchInput, setSearchInput] = useState(qFilter)
  const [opportunities, setOpportunities] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    setSearchInput(qFilter)
  }, [qFilter])

  useEffect(() => {
    const controller = new AbortController()
    setLoading(true)
    setError(null)
    listOpportunities({
      type: typeFilter || undefined,
      q: qFilter || undefined,
      signal: controller.signal,
    })
      .then((data) => setOpportunities(data || []))
      .catch((err) => {
        if (err.name === 'AbortError') return
        setError(err.message || 'Could not load opportunities.')
      })
      .finally(() => setLoading(false))
    return () => controller.abort()
  }, [typeFilter, qFilter])

  function updateParams(next) {
    const params = new URLSearchParams(searchParams)
    for (const [key, value] of Object.entries(next)) {
      if (value) params.set(key, value)
      else params.delete(key)
    }
    setSearchParams(params)
  }

  function handleTypeChange(e) {
    updateParams({ type: e.target.value })
  }

  function handleSearchSubmit(e) {
    e.preventDefault()
    updateParams({ q: searchInput.trim() })
  }

  function handleClearFilters() {
    setSearchInput('')
    setSearchParams(new URLSearchParams())
  }

  return (
    <div className="container py-4">
      <PageHeader
        title="Opportunities"
        subtitle="Every scholarship, internship, fellowship, event, and organization on The Yard."
        actions={
          <Link to="/opportunities/new" className="btn btn-primary">
            Add New
          </Link>
        }
      />

      <form
        className="row g-2 align-items-end mb-4 yard-filter-bar"
        onSubmit={handleSearchSubmit}
      >
        <div className="col-sm-4">
          <label htmlFor="typeFilter" className="form-label small mb-1">
            Filter by type
          </label>
          <select
            id="typeFilter"
            className="form-select"
            value={typeFilter}
            onChange={handleTypeChange}
          >
            <option value="">All types</option>
            {OPPORTUNITY_TYPES.map((t) => (
              <option key={t} value={t}>
                {t}
              </option>
            ))}
          </select>
        </div>
        <div className="col-sm-6">
          <label htmlFor="qFilter" className="form-label small mb-1">
            Search title or tags
          </label>
          <input
            id="qFilter"
            type="text"
            className="form-control"
            placeholder="e.g. STEM, leadership, summer"
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
          />
        </div>
        <div className="col-sm-2 d-flex gap-2">
          <button type="submit" className="btn btn-primary flex-grow-1">
            Search
          </button>
          {(typeFilter || qFilter) && (
            <button
              type="button"
              className="btn btn-outline-secondary"
              onClick={handleClearFilters}
              aria-label="Clear filters"
            >
              ×
            </button>
          )}
        </div>
      </form>

      {loading && (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading…</span>
          </div>
          <p className="mt-2 text-muted">Loading opportunities…</p>
        </div>
      )}

      {!loading && error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      {!loading && !error && opportunities.length === 0 && (
        <div className="alert alert-info text-center" role="status">
          <p className="mb-2">
            {typeFilter || qFilter
              ? 'No opportunities match those filters yet.'
              : 'No opportunities on The Yard yet.'}
          </p>
          <Link to="/opportunities/new" className="btn btn-primary btn-sm">
            Add the first one
          </Link>
        </div>
      )}

      {!loading && !error && opportunities.length > 0 && (
        <div className="row g-3">
          {opportunities.map((op) => (
            <div key={op.id} className="col-md-6 col-lg-4">
              <OpportunityCard opportunity={op} />
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default OpportunityListPage
