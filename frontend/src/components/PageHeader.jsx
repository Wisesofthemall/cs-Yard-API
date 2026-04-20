function PageHeader({ title, subtitle, actions }) {
  return (
    <header className="yard-page-header d-flex flex-column flex-md-row align-items-md-end justify-content-between gap-3 mb-4">
      <div>
        <h1 className="mb-1">{title}</h1>
        {subtitle && <p className="text-muted mb-0">{subtitle}</p>}
      </div>
      {actions && <div className="d-flex gap-2">{actions}</div>}
    </header>
  )
}

export default PageHeader
