package edu.famu.cop3060.yard.repository;

import edu.famu.cop3060.yard.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OpportunityRepository extends JpaRepository<Opportunity, String> {

    @Query(value = """
            SELECT COALESCE(MAX(CAST(SUBSTRING(id, 5) AS INTEGER)), 0)
            FROM opportunity
            WHERE id LIKE 'opp-%'
              AND LENGTH(id) >= 5
            """, nativeQuery = true)
    int findMaxOppNumericSuffix();

    @Query(value = """
            SELECT * FROM opportunity
            ORDER BY CAST(SUBSTRING(id, 5) AS INTEGER)
            """, nativeQuery = true)
    List<Opportunity> findAllOrderByNumericId();

    /**
     * Pass empty string for a dimension that should not filter (avoids NULL handling in SQL).
     */
    @Query(value = """
            SELECT o.* FROM opportunity o
            WHERE (LENGTH(TRIM(:typeFilter)) = 0
                   OR LOWER(TRIM(o.type)) = LOWER(TRIM(:typeFilter)))
              AND (LENGTH(TRIM(:qFilter)) = 0
                   OR LOWER(o.title) LIKE '%' || LOWER(TRIM(:qFilter)) || '%'
                   OR EXISTS (
                       SELECT 1 FROM opportunity_tag t
                       WHERE t.opportunity_id = o.id
                         AND LOWER(t.tag_value) LIKE '%' || LOWER(TRIM(:qFilter)) || '%'
                   ))
            ORDER BY CAST(SUBSTRING(o.id, 5) AS INTEGER)
            """, nativeQuery = true)
    List<Opportunity> findFiltered(
            @Param("typeFilter") String typeFilter,
            @Param("qFilter") String qFilter);
}
