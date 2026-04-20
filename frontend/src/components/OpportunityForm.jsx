import { useState } from 'react'
import { Link } from 'react-router-dom'
import { OPPORTUNITY_TYPES } from '../services/opportunityApi.js'

const EMPTY_FORM = {
  title: '',
  type: '',
  sponsor: '',
  deadline: '',
  description: '',
  tagsInput: '',
  url: '',
}

function toFormState(initialData) {
  if (!initialData) return EMPTY_FORM
  return {
    title: initialData.title ?? '',
    type: initialData.type ?? '',
    sponsor: initialData.sponsor ?? '',
    deadline: initialData.deadline ?? '',
    description: initialData.description ?? '',
    tagsInput: Array.isArray(initialData.tags)
      ? initialData.tags.join(', ')
      : '',
    url: initialData.url ?? '',
  }
}

function validateClient(formData) {
  const errors = {}
  if (!formData.title.trim()) errors.title = 'Title is required.'
  else if (formData.title.length > 120)
    errors.title = 'Title must be 120 characters or fewer.'

  if (!formData.type) errors.type = 'Select a type.'

  if (!formData.sponsor.trim()) errors.sponsor = 'Sponsor is required.'
  if (!formData.deadline.trim()) errors.deadline = 'Deadline is required.'

  if (!formData.description.trim())
    errors.description = 'Description is required.'
  else if (formData.description.length > 500)
    errors.description = 'Description must be 500 characters or fewer.'

  const tags = splitTags(formData.tagsInput)
  if (tags.length === 0) errors.tags = 'Add at least one tag.'

  if (!formData.url.trim()) errors.url = 'URL is required.'
  else if (!isValidUrl(formData.url))
    errors.url = 'Enter a valid URL (including https://).'

  return errors
}

function splitTags(input) {
  return input
    .split(',')
    .map((t) => t.trim())
    .filter(Boolean)
}

function isValidUrl(value) {
  try {
    const u = new URL(value)
    return u.protocol === 'http:' || u.protocol === 'https:'
  } catch {
    return false
  }
}

function OpportunityForm({
  initialData,
  onSubmit,
  submitLabel = 'Save',
  cancelHref = '/opportunities',
}) {
  const [formData, setFormData] = useState(() => toFormState(initialData))
  const [fieldErrors, setFieldErrors] = useState({})
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState(null)

  function handleChange(e) {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
    if (fieldErrors[name]) {
      setFieldErrors((prev) => {
        const next = { ...prev }
        delete next[name]
        return next
      })
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setSubmitError(null)
    const errors = validateClient(formData)
    if (Object.keys(errors).length > 0) {
      setFieldErrors(errors)
      return
    }

    const payload = {
      title: formData.title.trim(),
      type: formData.type,
      sponsor: formData.sponsor.trim(),
      deadline: formData.deadline.trim(),
      description: formData.description.trim(),
      tags: splitTags(formData.tagsInput),
      url: formData.url.trim(),
    }

    setSubmitting(true)
    setFieldErrors({})
    try {
      await onSubmit(payload)
    } catch (err) {
      if (err.status === 400 && err.body && typeof err.body === 'object') {
        setFieldErrors(err.body)
      } else {
        setSubmitError(err.message || 'Could not save changes.')
      }
      setSubmitting(false)
    }
  }

  const invalid = (field) => (fieldErrors[field] ? ' is-invalid' : '')

  return (
    <form
      className="yard-form card shadow-sm"
      noValidate
      onSubmit={handleSubmit}
    >
      <div className="card-body">
        {submitError && (
          <div className="alert alert-danger" role="alert">
            {submitError}
          </div>
        )}

        <div className="mb-3">
          <label htmlFor="title" className="form-label">
            Title <span className="text-danger">*</span>
          </label>
          <input
            id="title"
            name="title"
            type="text"
            className={'form-control' + invalid('title')}
            value={formData.title}
            onChange={handleChange}
            maxLength={120}
            required
          />
          {fieldErrors.title && (
            <div className="invalid-feedback d-block">
              {fieldErrors.title}
            </div>
          )}
        </div>

        <div className="row">
          <div className="col-md-6 mb-3">
            <label htmlFor="type" className="form-label">
              Type <span className="text-danger">*</span>
            </label>
            <select
              id="type"
              name="type"
              className={'form-select' + invalid('type')}
              value={formData.type}
              onChange={handleChange}
              required
            >
              <option value="">Choose a type…</option>
              {OPPORTUNITY_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
            {fieldErrors.type && (
              <div className="invalid-feedback d-block">
                {fieldErrors.type}
              </div>
            )}
          </div>

          <div className="col-md-6 mb-3">
            <label htmlFor="sponsor" className="form-label">
              Sponsor <span className="text-danger">*</span>
            </label>
            <input
              id="sponsor"
              name="sponsor"
              type="text"
              className={'form-control' + invalid('sponsor')}
              value={formData.sponsor}
              onChange={handleChange}
              required
            />
            {fieldErrors.sponsor && (
              <div className="invalid-feedback d-block">
                {fieldErrors.sponsor}
              </div>
            )}
          </div>
        </div>

        <div className="mb-3">
          <label htmlFor="deadline" className="form-label">
            Deadline <span className="text-danger">*</span>
          </label>
          <input
            id="deadline"
            name="deadline"
            type="text"
            className={'form-control' + invalid('deadline')}
            value={formData.deadline}
            onChange={handleChange}
            placeholder="e.g. 2025-04-15 or Rolling"
            required
          />
          {fieldErrors.deadline && (
            <div className="invalid-feedback d-block">
              {fieldErrors.deadline}
            </div>
          )}
        </div>

        <div className="mb-3">
          <label htmlFor="description" className="form-label">
            Description <span className="text-danger">*</span>
          </label>
          <textarea
            id="description"
            name="description"
            className={'form-control' + invalid('description')}
            rows={4}
            value={formData.description}
            onChange={handleChange}
            maxLength={500}
            required
          />
          <div className="form-text">
            {formData.description.length}/500 characters
          </div>
          {fieldErrors.description && (
            <div className="invalid-feedback d-block">
              {fieldErrors.description}
            </div>
          )}
        </div>

        <div className="mb-3">
          <label htmlFor="tagsInput" className="form-label">
            Tags <span className="text-danger">*</span>
          </label>
          <input
            id="tagsInput"
            name="tagsInput"
            type="text"
            className={'form-control' + invalid('tags')}
            value={formData.tagsInput}
            onChange={handleChange}
            placeholder="Comma-separated, e.g. STEM, paid, summer"
          />
          <div className="form-text">
            Separate tags with commas. At least one is required.
          </div>
          {fieldErrors.tags && (
            <div className="invalid-feedback d-block">
              {fieldErrors.tags}
            </div>
          )}
        </div>

        <div className="mb-4">
          <label htmlFor="url" className="form-label">
            URL <span className="text-danger">*</span>
          </label>
          <input
            id="url"
            name="url"
            type="url"
            className={'form-control' + invalid('url')}
            value={formData.url}
            onChange={handleChange}
            placeholder="https://example.com/opportunity"
            required
          />
          {fieldErrors.url && (
            <div className="invalid-feedback d-block">
              {fieldErrors.url}
            </div>
          )}
        </div>

        <div className="d-flex gap-2 justify-content-end">
          <Link to={cancelHref} className="btn btn-outline-secondary">
            Cancel
          </Link>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={submitting}
          >
            {submitting ? 'Saving…' : submitLabel}
          </button>
        </div>
      </div>
    </form>
  )
}

export default OpportunityForm
