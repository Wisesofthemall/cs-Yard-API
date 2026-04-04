package edu.famu.cop3060.yard.mapper;

import edu.famu.cop3060.yard.dto.OpportunityDTO;
import edu.famu.cop3060.yard.entity.Opportunity;

import java.util.List;

public final class OpportunityMapper {

    private OpportunityMapper() {
    }

    public static OpportunityDTO toDto(Opportunity entity) {
        List<String> tagList = entity.getTags() == null ? List.of() : List.copyOf(entity.getTags());
        return new OpportunityDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getType(),
                entity.getSponsor(),
                entity.getDeadline(),
                entity.getDescription(),
                tagList,
                entity.getUrl()
        );
    }
}
