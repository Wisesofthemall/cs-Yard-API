package edu.famu.cop3060.yard.service;

import edu.famu.cop3060.yard.dto.CreateOpportunityDTO;
import edu.famu.cop3060.yard.dto.OpportunityDTO;
import edu.famu.cop3060.yard.dto.UpdateOpportunityDTO;
import edu.famu.cop3060.yard.store.InMemoryOpportunityStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OpportunitiesService {

    private static final Logger log = LoggerFactory.getLogger(OpportunitiesService.class);

    /** Next numeric suffix for generated IDs (seed uses opp-001..opp-008). */
    private final AtomicInteger nextIdCounter = new AtomicInteger(9);

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

    public OpportunityDTO create(CreateOpportunityDTO dto) {
        String id = "opp-" + String.format("%03d", nextIdCounter.getAndIncrement());
        OpportunityDTO opportunity = new OpportunityDTO(
                id,
                dto.title(),
                dto.type(),
                dto.sponsor(),
                dto.deadline(),
                dto.description(),
                dto.tags(),
                dto.url()
        );
        OpportunityDTO saved = store.create(opportunity);
        log.info("Created opportunity with generated ID: {}", saved.id());
        return saved;
    }

    public Optional<OpportunityDTO> update(String id, UpdateOpportunityDTO dto) {
        OpportunityDTO opportunity = new OpportunityDTO(
                id,
                dto.title(),
                dto.type(),
                dto.sponsor(),
                dto.deadline(),
                dto.description(),
                dto.tags(),
                dto.url()
        );
        return store.update(id, opportunity);
    }

    public boolean delete(String id) {
        return store.delete(id);
    }
}
