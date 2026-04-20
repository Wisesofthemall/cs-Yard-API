import { useNavigate } from 'react-router-dom'
import PageHeader from '../components/PageHeader.jsx'
import OpportunityForm from '../components/OpportunityForm.jsx'
import { createOpportunity } from '../services/opportunityApi.js'
import { useAppContext } from '../context/AppContext.jsx'

function OpportunityCreatePage() {
  const navigate = useNavigate()
  const { showAlert } = useAppContext()

  async function handleSubmit(payload) {
    const created = await createOpportunity(payload)
    showAlert('success', `Created “${created.title}”.`)
    navigate(`/opportunities/${created.id}`)
  }

  return (
    <div className="container py-4">
      <PageHeader
        title="Add a new opportunity"
        subtitle="Fill out every field. The server will assign an ID like opp-009."
      />
      <OpportunityForm
        onSubmit={handleSubmit}
        submitLabel="Create opportunity"
      />
    </div>
  )
}

export default OpportunityCreatePage
