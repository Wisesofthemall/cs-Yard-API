package edu.famu.cop3060.yard.dto;

import java.util.List;

/**
 * Data Transfer Object for an Opportunity — scholarships, internships,
 * organizations, events, and fellowships relevant to HBCU students.
 */
public record OpportunityDTO(
        String id,
        String title,
        String type,
        String sponsor,
        String deadline,
        String description,
        List<String> tags,
        String url
) {}
