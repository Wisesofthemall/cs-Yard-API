const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

export class ApiError extends Error {
  constructor(message, { status, body } = {}) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.body = body
  }
}

async function request(path, { method = 'GET', body, signal } = {}) {
  let response
  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      method,
      headers: body ? { 'Content-Type': 'application/json' } : undefined,
      body: body ? JSON.stringify(body) : undefined,
      signal,
    })
  } catch (err) {
    if (err.name === 'AbortError') throw err
    throw new ApiError('Could not reach the server. Is the backend running?', {
      status: 0,
    })
  }

  if (response.status === 204) return null

  const text = await response.text()
  const payload = text ? safeParse(text) : null

  if (!response.ok) {
    const message =
      response.status === 404
        ? 'Opportunity not found.'
        : response.status === 400
          ? 'Please fix the highlighted fields.'
          : `Request failed with status ${response.status}.`
    throw new ApiError(message, { status: response.status, body: payload })
  }

  return payload
}

function safeParse(text) {
  try {
    return JSON.parse(text)
  } catch {
    return text
  }
}

export function listOpportunities({ type, q, signal } = {}) {
  const params = new URLSearchParams()
  if (type) params.set('type', type)
  if (q) params.set('q', q)
  const query = params.toString()
  return request(`/opportunities${query ? `?${query}` : ''}`, { signal })
}

export function getOpportunity(id, { signal } = {}) {
  return request(`/opportunities/${encodeURIComponent(id)}`, { signal })
}

export function createOpportunity(payload) {
  return request('/opportunities', { method: 'POST', body: payload })
}

export function updateOpportunity(id, payload) {
  return request(`/opportunities/${encodeURIComponent(id)}`, {
    method: 'PUT',
    body: payload,
  })
}

export function deleteOpportunity(id) {
  return request(`/opportunities/${encodeURIComponent(id)}`, {
    method: 'DELETE',
  })
}

export const OPPORTUNITY_TYPES = [
  'Scholarship',
  'Internship',
  'Organization',
  'Event',
  'Fellowship',
]
