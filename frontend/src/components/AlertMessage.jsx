import { useEffect } from 'react'
import { useAppContext } from '../context/AppContext.jsx'

function AlertMessage() {
  const { alert, clearAlert } = useAppContext()

  useEffect(() => {
    if (!alert || alert.type !== 'success') return
    const timer = setTimeout(clearAlert, 5000)
    return () => clearTimeout(timer)
  }, [alert, clearAlert])

  if (!alert) return null

  const variant =
    alert.type === 'danger'
      ? 'alert-danger'
      : alert.type === 'warning'
        ? 'alert-warning'
        : alert.type === 'info'
          ? 'alert-info'
          : 'alert-success'

  return (
    <div className="container mt-3">
      <div
        className={`alert ${variant} alert-dismissible fade show yard-alert`}
        role="alert"
      >
        {alert.message}
        <button
          type="button"
          className="btn-close"
          aria-label="Close"
          onClick={clearAlert}
        />
      </div>
    </div>
  )
}

export default AlertMessage
