package edu.famu.cop3060.yard.controller;

import edu.famu.cop3060.yard.dto.CreateOpportunityDTO;
import edu.famu.cop3060.yard.dto.OpportunityDTO;
import edu.famu.cop3060.yard.dto.UpdateOpportunityDTO;
import edu.famu.cop3060.yard.service.OpportunitiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunitiesController {

    private static final Logger log = LoggerFactory.getLogger(OpportunitiesController.class);

    private final OpportunitiesService opportunitiesService;

    public OpportunitiesController(OpportunitiesService opportunitiesService) {
        this.opportunitiesService = opportunitiesService;
    }

    @GetMapping
    public ResponseEntity<List<OpportunityDTO>> list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String q) {
        String typeDisplay = (type == null || type.isBlank()) ? "<empty>" : type;
        String qDisplay = (q == null || q.isBlank()) ? "<empty>" : q;
        log.info("GET /api/opportunities — type={}, q={}", typeDisplay, qDisplay);

        List<OpportunityDTO> opportunities = opportunitiesService.getAllOpportunities(type, q);
        return ResponseEntity.ok(opportunities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpportunityDTO> getById(@PathVariable String id) {
        log.info("GET /api/opportunities/{}", id);

        return opportunitiesService.getOpportunityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OpportunityDTO> create(@Valid @RequestBody CreateOpportunityDTO body) {
        log.info("POST /api/opportunities — title={}", body.title());
        OpportunityDTO saved = opportunitiesService.create(body);
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.id()).toUri())
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpportunityDTO> update(@PathVariable String id, @Valid @RequestBody UpdateOpportunityDTO body) {
        log.info("PUT /api/opportunities/{}", id);
        return opportunitiesService.update(id, body)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("PUT /api/opportunities/{} — update attempted on nonexistent record", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/opportunities/{}", id);
        if (opportunitiesService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        log.warn("DELETE /api/opportunities/{} — deletion attempted on nonexistent record", id);
        return ResponseEntity.notFound().build();
    }
}
