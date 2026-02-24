package edu.famu.cop3060.yard.controller;

import edu.famu.cop3060.yard.dto.OpportunityDTO;
import edu.famu.cop3060.yard.service.OpportunitiesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpportunitiesController.class)
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
}
