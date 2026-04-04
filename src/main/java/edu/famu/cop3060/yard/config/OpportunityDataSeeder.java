package edu.famu.cop3060.yard.config;

import edu.famu.cop3060.yard.repository.OpportunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Loads the same seed opportunities as the former in-memory store when the database is empty.
 */
@Component
public class OpportunityDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OpportunityDataSeeder.class);

    private final OpportunityRepository opportunityRepository;

    public OpportunityDataSeeder(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    @Override
    public void run(String... args) {
        if (opportunityRepository.count() > 0) {
            return;
        }
        var seed = OpportunitySeedData.all();
        opportunityRepository.saveAll(seed);
        log.info("Seeded {} opportunities into the database.", seed.size());
    }
}
