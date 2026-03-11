package edu.famu.cop3060.yard.controller;

import edu.famu.cop3060.yard.dto.OpportunityDTO;
import edu.famu.cop3060.yard.service.OpportunitiesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpportunitiesController.class)
@Import(ValidationExceptionHandler.class)
class OpportunitiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OpportunitiesService opportunitiesService;

    @Test
    void list_returns200AndJsonArray() throws Exception {
        OpportunityDTO opp1 = new OpportunityDTO(
                "opp-001",
                "UNCF STEM Scholarship",
                "Scholarship",
                "UNCF",
                "2025-04-15",
                "Merit-based scholarship for STEM majors.",
                List.of("STEM", "undergrad"),
                "https://uncf.org/stem"
        );
        OpportunityDTO opp2 = new OpportunityDTO(
                "opp-002",
                "Google HBCU Career Residency",
                "Fellowship",
                "Google",
                "2025-03-01",
                "Paid residency for recent HBCU grads.",
                List.of("tech", "paid"),
                "https://careers.google.com/hbcu"
        );
        when(opportunitiesService.getAllOpportunities(eq(null), eq(null)))
                .thenReturn(List.of(opp1, opp2));

        mockMvc.perform(get("/api/opportunities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("UNCF STEM Scholarship"))
                .andExpect(jsonPath("$[1].id").value("opp-002"));
    }

    @Test
    void getById_returns200AndCorrectTitle_whenIdExists() throws Exception {
        OpportunityDTO opp = new OpportunityDTO(
                "opp-001",
                "UNCF STEM Scholarship",
                "Scholarship",
                "UNCF",
                "2025-04-15",
                "Merit-based scholarship for STEM majors.",
                List.of("STEM", "undergrad"),
                "https://uncf.org/stem"
        );
        when(opportunitiesService.getOpportunityById("opp-001")).thenReturn(Optional.of(opp));

        mockMvc.perform(get("/api/opportunities/opp-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("UNCF STEM Scholarship"))
                .andExpect(jsonPath("$.id").value("opp-001"));
    }

    @Test
    void getById_returns404_whenIdDoesNotExist() throws Exception {
        when(opportunitiesService.getOpportunityById("opp-999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/opportunities/opp-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void post_returns201CreatedWithNewRecordAndLocationHeader() throws Exception {
        String requestBody = """
                {
                    "title": "New Scholarship",
                    "type": "Scholarship",
                    "sponsor": "Test Org",
                    "deadline": "2025-12-31",
                    "description": "A test opportunity.",
                    "tags": ["test"],
                    "url": "https://example.com/scholarship"
                }
                """;
        OpportunityDTO saved = new OpportunityDTO(
                "opp-009",
                "New Scholarship",
                "Scholarship",
                "Test Org",
                "2025-12-31",
                "A test opportunity.",
                List.of("test"),
                "https://example.com/scholarship"
        );
        when(opportunitiesService.create(any())).thenReturn(saved);

        mockMvc.perform(post("/api/opportunities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("opp-009"))
                .andExpect(jsonPath("$.title").value("New Scholarship"))
                .andExpect(header().string("Location", "http://localhost/api/opportunities/opp-009"));
    }

    @Test
    void put_returns200OkWithUpdatedRecord_whenIdExists() throws Exception {
        String requestBody = """
                {
                    "title": "Updated Title",
                    "type": "Scholarship",
                    "sponsor": "Updated Sponsor",
                    "deadline": "2025-06-01",
                    "description": "Updated description.",
                    "tags": ["updated"],
                    "url": "https://example.com/updated"
                }
                """;
        OpportunityDTO updated = new OpportunityDTO(
                "opp-001",
                "Updated Title",
                "Scholarship",
                "Updated Sponsor",
                "2025-06-01",
                "Updated description.",
                List.of("updated"),
                "https://example.com/updated"
        );
        when(opportunitiesService.update(eq("opp-001"), any())).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/opportunities/opp-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("opp-001"))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.sponsor").value("Updated Sponsor"));
    }

    @Test
    void delete_returns204NoContent_whenIdExists() throws Exception {
        when(opportunitiesService.delete("opp-001")).thenReturn(true);

        mockMvc.perform(delete("/api/opportunities/opp-001"))
                .andExpect(status().isNoContent())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    assert content.isEmpty();
                });
    }

    @Test
    void post_returns400BadRequestWithFieldErrors_whenRequiredFieldMissing() throws Exception {
        String requestBody = """
                {
                    "type": "Scholarship",
                    "sponsor": "Test Org",
                    "deadline": "2025-12-31",
                    "description": "A test.",
                    "tags": ["test"],
                    "url": "https://example.com"
                }
                """;

        mockMvc.perform(post("/api/opportunities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").exists());
    }
}
