import { useEffect, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import PageHeader from '../components/PageHeader.jsx'
import ConfirmDeleteModal from '../components/ConfirmDeleteModal.jsx'
import {
  deleteOpportunity,
  getOpportunity,
} from '../services/opportunityApi.js'

function OpportunityDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [opportunity, setOpportunity] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [showDeleteModal, setShowDeleteModal] = useState(false)
  const [deleting, setDeleting] = useState(false)
  const [deleteError, setDeleteError] = useState(null)

  useEffect(() => {
    const controller = new AbortController()
    setLoading(true)
    setError(null)
    getOpportunity(id, { signal: controller.signal })
      .then(setOpportunity)
      .catch((err) => {
        if (err.name === 'AbortError') return
        setError(err.message || 'Could not load this opportunity.')
      })
      .finally(() => setLoading(false))
    return () => controller.abort()
  }, [id])

  async function handleConfirmDelete() {
    setDeleting(true)
    setDeleteError(null)
    try {
      await deleteOpportunity(id)
      navigate('/opportunities', {
        state: { flash: `Deleted “${opportunity.title}”.` },
      })
    } catch (err) {
      setDeleteError(err.message || 'Could not delete this opportunity.')
      setDeleting(false)
    }
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

  if (error) {
    return (
      <div className="container py-4">
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
        <Link to="/opportunities" className="btn btn-outline-secondary">
          ← Back to list
        </Link>
      </div>
    )
  }

  if (!opportunity) return null

  return (
    <div className="container py-4">
      <PageHeader
        title={opportunity.title}
        subtitle={`${opportunity.type} · ${opportunity.sponsor}`}
        actions={
          <>
            <Link
              to="/opportunities"
              className="btn btn-outline-secondary"
            >
              ← Back
            </Link>
            <Link
              to={`/opportunities/${opportunity.id}/edit`}
              className="btn btn-primary"
            >
              Edit
            </Link>
            <button
              type="button"
              className="btn btn-outline-danger"
              onClick={() => {
                setDeleteError(null)
                setShowDeleteModal(true)
              }}
            >
              Delete
            </button>
          </>
        }
      />

      {deleteError && (
        <div className="alert alert-danger" role="alert">
          {deleteError}
        </div>
      )}

      <div className="card shadow-sm yard-detail-card">
        <div className="card-body">
          <dl className="row mb-0">
            <dt className="col-sm-3">ID</dt>
            <dd className="col-sm-9"><code>{opportunity.id}</code></dd>

            <dt className="col-sm-3">Type</dt>
            <dd className="col-sm-9">
              <span className="badge yard-type-badge">
                {opportunity.type}
              </span>
            </dd>

            <dt className="col-sm-3">Sponsor</dt>
            <dd className="col-sm-9">{opportunity.sponsor}</dd>

            <dt className="col-sm-3">Deadline</dt>
            <dd className="col-sm-9">{opportunity.deadline || '—'}</dd>

            <dt className="col-sm-3">Description</dt>
            <dd className="col-sm-9">{opportunity.description}</dd>

            <dt className="col-sm-3">Tags</dt>
            <dd className="col-sm-9">
              {opportunity.tags && opportunity.tags.length > 0 ? (
                <div className="d-flex flex-wrap gap-1">
                  {opportunity.tags.map((tag) => (
                    <span
                      key={tag}
                      className="badge bg-light text-dark border"
                    >
                      {tag}
                    </span>
                  ))}
                </div>
              ) : (
                '—'
              )}
            </dd>

            <dt className="col-sm-3">Link</dt>
            <dd className="col-sm-9">
              <a
                href={opportunity.url}
                target="_blank"
                rel="noreferrer"
              >
                {opportunity.url}
              </a>
            </dd>
          </dl>
        </div>
      </div>

      <ConfirmDeleteModal
        open={showDeleteModal}
        title="Delete this opportunity?"
        message={
          <>
            You are about to permanently remove{' '}
            <strong>{opportunity.title}</strong> (<code>{opportunity.id}</code>).
            This action cannot be undone.
          </>
        }
        onCancel={() => setShowDeleteModal(false)}
        onConfirm={handleConfirmDelete}
        busy={deleting}
      />
    </div>
  )
}

export default OpportunityDetailPage
