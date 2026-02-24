package edu.famu.cop3060.yard.controller;

import edu.famu.cop3060.yard.dto.OpportunityDTO;
import edu.famu.cop3060.yard.service.OpportunitiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
