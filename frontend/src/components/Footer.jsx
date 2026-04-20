function Footer() {
  const year = new Date().getFullYear()
  return (
    <footer className="yard-footer mt-auto py-4">
      <div className="container d-flex flex-column flex-md-row justify-content-between align-items-center gap-2">
        <span className="small">
          © {year} The Yard API · Built for COP3060 PA05
        </span>
        <a
          className="small text-decoration-none"
          href="https://github.com/cis-famu/cs-Yard-API"
          target="_blank"
          rel="noreferrer"
        >
          View source on GitHub
        </a>
      </div>
    </footer>
  )
}

export default Footer
