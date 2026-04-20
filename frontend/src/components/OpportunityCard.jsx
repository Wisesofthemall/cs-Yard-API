import { Link } from 'react-router-dom'

function OpportunityCard({ opportunity, onDelete }) {
  const { id, title, type, sponsor, deadline, description, tags } = opportunity
  const truncated =
    description && description.length > 140
      ? description.slice(0, 137) + '…'
      : description

  return (
    <div className="card h-100 shadow-sm yard-opportunity-card">
      <div className="card-body d-flex flex-column">
        <div className="d-flex justify-content-between align-items-start gap-2 mb-2">
          <h3 className="h5 card-title mb-0">{title}</h3>
          <span className="badge yard-type-badge">{type}</span>
        </div>
        <p className="text-muted small mb-2">
          <strong>{sponsor}</strong> · Deadline {deadline}
        </p>
        <p className="card-text mb-3">{truncated}</p>
        {tags && tags.length > 0 && (
          <div className="mb-3 d-flex flex-wrap gap-1">
            {tags.map((tag) => (
              <span key={tag} className="badge bg-light text-dark border">
                {tag}
              </span>
            ))}
          </div>
        )}
        <div className="mt-auto d-flex flex-wrap gap-2">
          <Link
            to={`/opportunities/${id}`}
            className="btn btn-sm btn-primary"
          >
            View
          </Link>
          <Link
            to={`/opportunities/${id}/edit`}
            className="btn btn-sm btn-outline-secondary"
          >
            Edit
          </Link>
          {onDelete && (
            <button
              type="button"
              className="btn btn-sm btn-outline-danger"
              onClick={() => onDelete(opportunity)}
            >
              Delete
            </button>
          )}
        </div>
      </div>
    </div>
  )
}

export default OpportunityCard
