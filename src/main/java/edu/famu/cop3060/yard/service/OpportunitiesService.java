package edu.famu.cop3060.yard.service;

import edu.famu.cop3060.yard.dto.CreateOpportunityDTO;
import edu.famu.cop3060.yard.dto.OpportunityDTO;
import edu.famu.cop3060.yard.dto.UpdateOpportunityDTO;
import edu.famu.cop3060.yard.entity.Opportunity;
import edu.famu.cop3060.yard.mapper.OpportunityMapper;
import edu.famu.cop3060.yard.repository.OpportunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OpportunitiesService {

    private static final Logger log = LoggerFactory.getLogger(OpportunitiesService.class);

    private final OpportunityRepository opportunityRepository;

    public OpportunitiesService(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    @Transactional(readOnly = true)
    public List<OpportunityDTO> getAllOpportunities(String type, String q) {
        boolean hasType = type != null && !type.isBlank();
        boolean hasQ = q != null && !q.isBlank();
        if (!hasType && !hasQ) {
            return opportunityRepository.findAllOrderByNumericId().stream()
                    .map(OpportunityMapper::toDto)
                    .toList();
        }
        String typeFilter = hasType ? type.trim() : "";
        String qFilter = hasQ ? q.trim() : "";
        return opportunityRepository.findFiltered(typeFilter, qFilter).stream()
                .map(OpportunityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<OpportunityDTO> getOpportunityById(String id) {
        return opportunityRepository.findById(id).map(OpportunityMapper::toDto);
    }

    @Transactional
    public OpportunityDTO create(CreateOpportunityDTO dto) {
        int nextSuffix = opportunityRepository.findMaxOppNumericSuffix() + 1;
        String id = "opp-" + String.format("%03d", nextSuffix);
        Opportunity entity = new Opportunity(
                id,
                dto.title(),
                dto.type(),
                dto.sponsor(),
                dto.deadline(),
                dto.description(),
                dto.tags(),
                dto.url()
        );
        Opportunity saved = opportunityRepository.save(entity);
        log.info("Created opportunity with generated ID: {}", saved.getId());
        return OpportunityMapper.toDto(saved);
    }

    @Transactional
    public Optional<OpportunityDTO> update(String id, UpdateOpportunityDTO dto) {
        return opportunityRepository.findById(id).map(entity -> {
            entity.setTitle(dto.title());
            entity.setType(dto.type());
            entity.setSponsor(dto.sponsor());
            entity.setDeadline(dto.deadline());
            entity.setDescription(dto.description());
            entity.setUrl(dto.url());
            entity.getTags().clear();
            entity.getTags().addAll(dto.tags());
            return OpportunityMapper.toDto(entity);
        });
    }

    @Transactional
    public boolean delete(String id) {
        if (!opportunityRepository.existsById(id)) {
            return false;
        }
        opportunityRepository.deleteById(id);
        return true;
    }
}
