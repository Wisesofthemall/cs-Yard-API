import { useEffect } from 'react'

function ConfirmDeleteModal({
  open,
  title,
  message,
  onCancel,
  onConfirm,
  busy = false,
}) {
  useEffect(() => {
    if (!open) return
    function onKey(e) {
      if (e.key === 'Escape' && !busy) onCancel()
    }
    document.addEventListener('keydown', onKey)
    document.body.classList.add('modal-open')
    return () => {
      document.removeEventListener('keydown', onKey)
      document.body.classList.remove('modal-open')
    }
  }, [open, busy, onCancel])

  if (!open) return null

  return (
    <>
      <div
        className="modal show d-block"
        role="dialog"
        aria-modal="true"
        aria-labelledby="confirmDeleteTitle"
        tabIndex="-1"
        onClick={(e) => {
          if (e.target === e.currentTarget && !busy) onCancel()
        }}
      >
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content">
            <div className="modal-header">
              <h2 className="modal-title h5" id="confirmDeleteTitle">
                {title || 'Confirm deletion'}
              </h2>
              <button
                type="button"
                className="btn-close"
                aria-label="Close"
                onClick={onCancel}
                disabled={busy}
              />
            </div>
            <div className="modal-body">
              <p className="mb-0">{message}</p>
            </div>
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={onCancel}
                disabled={busy}
              >
                Cancel
              </button>
              <button
                type="button"
                className="btn btn-danger"
                onClick={onConfirm}
                disabled={busy}
              >
                {busy ? 'Deleting…' : 'Delete'}
              </button>
            </div>
          </div>
        </div>
      </div>
      <div className="modal-backdrop show" />
    </>
  )
}

export default ConfirmDeleteModal
