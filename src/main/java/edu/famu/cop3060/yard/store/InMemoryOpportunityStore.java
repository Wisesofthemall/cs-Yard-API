package edu.famu.cop3060.yard.store;

import edu.famu.cop3060.yard.dto.OpportunityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class InMemoryOpportunityStore {

    private static final Logger log = LoggerFactory.getLogger(InMemoryOpportunityStore.class);

    private final Map<String, OpportunityDTO> byId;
    private final List<OpportunityDTO> all;

    public InMemoryOpportunityStore() {
        List<OpportunityDTO> seed = buildSeedData();
        this.byId = new HashMap<>(seed.stream().collect(Collectors.toMap(OpportunityDTO::id, o -> o)));
        this.all = new ArrayList<>(seed);
        log.info("Seeded {} opportunities into the in-memory store.", all.size());
    }

    public List<OpportunityDTO> findAll() {
        return Collections.unmodifiableList(all);
    }

    public Optional<OpportunityDTO> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public List<OpportunityDTO> findFiltered(String type, String q) {
        Stream<OpportunityDTO> stream = all.stream();
        if (type != null && !type.isBlank()) {
            String typeLower = type.trim().toLowerCase();
            stream = stream.filter(o -> o.type() != null && o.type().toLowerCase().equals(typeLower));
        }
        if (q != null && !q.isBlank()) {
            String qLower = q.trim().toLowerCase();
            stream = stream.filter(o ->
                    (o.title() != null && o.title().toLowerCase().contains(qLower))
                            || (o.tags() != null && o.tags().stream().anyMatch(t -> t != null && t.toLowerCase().contains(qLower)))
            );
        }
        return stream.toList();
    }

    /**
     * Adds a new opportunity (with ID already set) to the store. Keeps map and list in sync.
     */
    public OpportunityDTO create(OpportunityDTO dto) {
        byId.put(dto.id(), dto);
        all.add(dto);
        return dto;
    }

    /**
     * Replaces an existing opportunity by id. Returns empty if the id does not exist.
     */
    public Optional<OpportunityDTO> update(String id, OpportunityDTO dto) {
        if (!byId.containsKey(id)) {
            return Optional.empty();
        }
        byId.put(id, dto);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).id().equals(id)) {
                all.set(i, dto);
                break;
            }
        }
        return Optional.of(dto);
    }

    /**
     * Removes an opportunity by id. Returns true if removed, false if id did not exist.
     */
    public boolean delete(String id) {
        if (!byId.containsKey(id)) {
            return false;
        }
        byId.remove(id);
        all.removeIf(o -> o.id().equals(id));
        return true;
    }

    private static List<OpportunityDTO> buildSeedData() {
        return List.of(
                new OpportunityDTO(
                        "opp-001",
                        "UNCF STEM Scholarship",
                        "Scholarship",
                        "UNCF",
                        "2025-04-15",
                        "Merit-based scholarship for STEM majors at HBCUs.",
                        List.of("STEM", "undergrad", "paid"),
                        "https://uncf.org/programs/uncf-stem-scholarship"
                ),
                new OpportunityDTO(
                        "opp-002",
                        "Google HBCU Career Residency",
                        "Fellowship",
                        "Google",
                        "2025-03-01",
                        "Paid residency program for recent HBCU grads in tech.",
                        List.of("tech", "paid", "summer"),
                        "https://careers.google.com/students/hbcu"
                ),
                new OpportunityDTO(
                        "opp-003",
                        "National Society of Black Engineers Chapter",
                        "Organization",
                        "NSBE",
                        "",
                        "Student chapter for networking and professional development in engineering.",
                        List.of("engineering", "networking", "STEM"),
                        "https://www.nsbe.org"
                ),
                new OpportunityDTO(
                        "opp-004",
                        "Homecoming Step Show Registration",
                        "Event",
                        "Student Government",
                        "2025-10-01",
                        "Register your step team for the annual homecoming step show.",
                        List.of("culture", "performing arts", "campus"),
                        "https://example.edu/homecoming/stepshow"
                ),
                new OpportunityDTO(
                        "opp-005",
                        "Goldman Sachs Summer Analyst Program",
                        "Internship",
                        "Goldman Sachs",
                        "2025-01-15",
                        "Summer analyst internship for juniors and seniors in finance.",
                        List.of("finance", "paid", "junior", "senior"),
                        "https://www.goldmansachs.com/careers/students"
                ),
                new OpportunityDTO(
                        "opp-006",
                        "Thurgood Marshall College Fund Scholarship",
                        "Scholarship",
                        "TMCF",
                        "2025-05-01",
                        "Scholarships for students attending TMCF member schools.",
                        List.of("undergrad", "graduate", "leadership"),
                        "https://www.tmcf.org/scholarships"
                ),
                new OpportunityDTO(
                        "opp-007",
                        "Microsoft LEAP Engineering Acceleration Program",
                        "Fellowship",
                        "Microsoft",
                        "2025-02-28",
                        "Technical fellowship for career switchers and bootcamp grads.",
                        List.of("tech", "paid", "engineering"),
                        "https://careers.microsoft.com/leap"
                ),
                new OpportunityDTO(
                        "opp-008",
                        "Black Engineer of the Year Award Conference",
                        "Event",
                        "BEYA",
                        "2025-01-31",
                        "Annual conference and career fair for STEM professionals and students.",
                        List.of("STEM", "networking", "career fair"),
                        "https://www.beya.org"
                )
        );
    }
}
