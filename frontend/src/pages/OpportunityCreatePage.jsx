import { useNavigate } from 'react-router-dom'
import PageHeader from '../components/PageHeader.jsx'
import OpportunityForm from '../components/OpportunityForm.jsx'
import { createOpportunity } from '../services/opportunityApi.js'

function OpportunityCreatePage() {
  const navigate = useNavigate()

  async function handleSubmit(payload) {
    const created = await createOpportunity(payload)
    navigate(`/opportunities/${created.id}`, {
      state: { flash: `Created “${created.title}”.` },
    })
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
