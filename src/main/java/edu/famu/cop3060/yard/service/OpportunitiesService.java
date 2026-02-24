package edu.famu.cop3060.yard.service;

import edu.famu.cop3060.yard.dto.OpportunityDTO;
import edu.famu.cop3060.yard.store.InMemoryOpportunityStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpportunitiesService {

    private final InMemoryOpportunityStore store;

    public OpportunitiesService(InMemoryOpportunityStore store) {
        this.store = store;
    }

    public List<OpportunityDTO> getAllOpportunities(String type, String q) {
        if ((type == null || type.isBlank()) && (q == null || q.isBlank())) {
            return store.findAll();
        }
        return store.findFiltered(type, q);
    }

    public Optional<OpportunityDTO> getOpportunityById(String id) {
        return store.findById(id);
    }
}
