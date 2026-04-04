package edu.famu.cop3060.yard;

import edu.famu.cop3060.yard.config.OpportunitySeedData;
import edu.famu.cop3060.yard.repository.OpportunityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpportunitiesApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @BeforeEach
    void resetDatabase() {
        opportunityRepository.deleteAll();
        opportunityRepository.saveAll(OpportunitySeedData.all());
    }

    @Test
    void list_afterSeed_returnsAtLeastEightOpportunities() throws Exception {
        mockMvc.perform(get("/api/opportunities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(8)));
    }

    @Test
    void getById_returns200_whenSeededIdExists() throws Exception {
        mockMvc.perform(get("/api/opportunities/opp-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("opp-001"))
                .andExpect(jsonPath("$.title").value("UNCF STEM Scholarship"));
    }

    @Test
    void getById_returns404_whenIdMissing() throws Exception {
        mockMvc.perform(get("/api/opportunities/opp-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_thenGet_persistsNewOpportunityWithNextId() throws Exception {
        String body = """
                {
                    "title": "Integration Test Scholarship",
                    "type": "Scholarship",
                    "sponsor": "Test Foundation",
                    "deadline": "2026-12-31",
                    "description": "Created during integration test.",
                    "tags": ["integration", "test"],
                    "url": "https://example.com/integration-scholarship"
                }
                """;
        mockMvc.perform(post("/api/opportunities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("opp-009"))
                .andExpect(jsonPath("$.title").value("Integration Test Scholarship"));

        mockMvc.perform(get("/api/opportunities/opp-009"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags[0]").value("integration"))
                .andExpect(jsonPath("$.tags[1]").value("test"));
    }

    @Test
    void update_persistsChanges() throws Exception {
        String createBody = """
                {
                    "title": "To Update",
                    "type": "Event",
                    "sponsor": "Org",
                    "deadline": "2026-06-01",
                    "description": "Will be updated.",
                    "tags": ["old"],
                    "url": "https://example.com/event"
                }
                """;
        mockMvc.perform(post("/api/opportunities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("opp-009"));

        String updateBody = """
                {
                    "title": "Updated Title",
                    "type": "Event",
                    "sponsor": "Org",
                    "deadline": "2026-06-15",
                    "description": "Updated description.",
                    "tags": ["new", "tags"],
                    "url": "https://example.com/updated"
                }
                """;
        mockMvc.perform(put("/api/opportunities/opp-009")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.tags.length()").value(2));

        mockMvc.perform(get("/api/opportunities/opp-009"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description."));
    }

    @Test
    void delete_removesOpportunity() throws Exception {
        String createBody = """
                {
                    "title": "To Delete",
                    "type": "Fellowship",
                    "sponsor": "Org",
                    "deadline": "2026-07-01",
                    "description": "Temporary row.",
                    "tags": ["temp"],
                    "url": "https://example.com/delete-me"
                }
                """;
        mockMvc.perform(post("/api/opportunities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/opportunities/opp-009"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/opportunities/opp-009"))
                .andExpect(status().isNotFound());
    }

    @Test
    void list_filterByType_caseInsensitive() throws Exception {
        mockMvc.perform(get("/api/opportunities").param("type", "scholarship"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].type", everyItem(is("Scholarship"))));
    }

    @Test
    void list_filterByKeyword_matchesTitleOrTag() throws Exception {
        mockMvc.perform(get("/api/opportunities").param("q", "STEM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(1)));
    }

    @Test
    void put_returns404_whenIdDoesNotExist() throws Exception {
        String body = """
                {
                    "title": "Ghost",
                    "type": "Scholarship",
                    "sponsor": "X",
                    "deadline": "2026-01-01",
                    "description": "None.",
                    "tags": ["x"],
                    "url": "https://example.com/x"
                }
                """;
        mockMvc.perform(put("/api/opportunities/opp-999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns404_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/opportunities/opp-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void post_returns400_whenValidationFails() throws Exception {
        String body = """
                {
                    "type": "Scholarship",
                    "sponsor": "Test Org",
                    "deadline": "2026-12-31",
                    "description": "Missing title.",
                    "tags": ["test"],
                    "url": "https://example.com"
                }
                """;
        mockMvc.perform(post("/api/opportunities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }
}
