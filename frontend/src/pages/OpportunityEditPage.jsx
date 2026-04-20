import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import PageHeader from '../components/PageHeader.jsx'
import OpportunityForm from '../components/OpportunityForm.jsx'
import {
  getOpportunity,
  updateOpportunity,
} from '../services/opportunityApi.js'
import { useAppContext } from '../context/AppContext.jsx'

function OpportunityEditPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { showAlert } = useAppContext()
  const [opportunity, setOpportunity] = useState(null)
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState(null)

  useEffect(() => {
    const controller = new AbortController()
    setLoading(true)
    setLoadError(null)
    getOpportunity(id, { signal: controller.signal })
      .then(setOpportunity)
      .catch((err) => {
        if (err.name === 'AbortError') return
        setLoadError(err.message || 'Could not load this opportunity.')
      })
      .finally(() => setLoading(false))
    return () => controller.abort()
  }, [id])

  async function handleSubmit(payload) {
    const updated = await updateOpportunity(id, payload)
    showAlert('success', `Updated “${updated.title}”.`)
    navigate(`/opportunities/${id}`)
  }

  if (loading) {
    return (
      <div className="container py-5 text-center">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading…</span>
        </div>
      </div>
    )
  }

  if (loadError) {
    return (
      <div className="container py-4">
        <div className="alert alert-danger" role="alert">
          {loadError}
        </div>
        <Link to="/opportunities" className="btn btn-outline-secondary">
          ← Back to list
        </Link>
      </div>
    )
  }

  return (
    <div className="container py-4">
      <PageHeader
        title={`Edit: ${opportunity.title}`}
        subtitle={`Editing ${opportunity.id}`}
      />
      <OpportunityForm
        initialData={opportunity}
        onSubmit={handleSubmit}
        submitLabel="Save changes"
        cancelHref={`/opportunities/${id}`}
      />
    </div>
  )
}

export default OpportunityEditPage
