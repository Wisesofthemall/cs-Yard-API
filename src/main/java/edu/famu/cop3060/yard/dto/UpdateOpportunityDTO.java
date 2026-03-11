package edu.famu.cop3060.yard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;

/**
 * Request body for PUT /api/opportunities/{id}. Contains all fields required to
 * replace an existing opportunity. The id is supplied in the URL path, not in the body.
 */
public record UpdateOpportunityDTO(
        @NotBlank(message = "title must not be blank")
        @Size(max = 120, message = "title must be at most 120 characters")
        String title,

        @NotBlank(message = "type must not be blank")
        @Pattern(regexp = "^(Scholarship|Internship|Organization|Event|Fellowship)$",
                message = "type must be one of: Scholarship, Internship, Organization, Event, Fellowship")
        String type,

        @NotBlank(message = "sponsor must not be blank")
        String sponsor,

        @NotBlank(message = "deadline must not be blank")
        String deadline,

        @NotBlank(message = "description must not be blank")
        @Size(max = 500, message = "description must be at most 500 characters")
        String description,

        @NotNull(message = "tags must not be null")
        @Size(min = 1, message = "tags must contain at least one entry")
        List<String> tags,

        @NotBlank(message = "url must not be blank")
        @URL(message = "url must be a well-formed URL")
        String url
) {}
