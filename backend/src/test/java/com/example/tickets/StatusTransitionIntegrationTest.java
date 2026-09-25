package com.example.tickets;

import com.example.tickets.domain.TicketStatus;

import static com.example.tickets.domain.TicketStatus.CLOSED;
import static com.example.tickets.domain.TicketStatus.IN_PROGRESS;
import static com.example.tickets.domain.TicketStatus.RESOLVED;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatusTransitionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID ticketId;

    @BeforeEach
    void createTicket() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Integration ticket",
                                  "description": "State machine test",
                                  "priority": "HIGH"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        ticketId = UUID.fromString(body.get("id").asText());
    }

    @Test
    void shouldFollowValidTransitionChain() throws Exception {
        transitionExpectOk(IN_PROGRESS);
        transitionExpectOk(RESOLVED);
        transitionExpectOk(CLOSED);
    }

    @Test
    void shouldRejectClosedToOpen() throws Exception {
        transitionExpectOk(IN_PROGRESS);
        transitionExpectOk(RESOLVED);
        transitionExpectOk(CLOSED);

        mockMvc.perform(patch("/api/v1/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("CLOSED")));
    }

    @Test
    void shouldRejectResolvedToOpen() throws Exception {
        transitionExpectOk(IN_PROGRESS);
        transitionExpectOk(RESOLVED);

        mockMvc.perform(patch("/api/v1/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldRejectCancelledToOpen() throws Exception {
        mockMvc.perform(patch("/api/v1/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CANCELLED\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldRejectOpenToClosed() throws Exception {
        mockMvc.perform(patch("/api/v1/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CLOSED\"}"))
                .andExpect(status().isConflict());
    }

    private void transitionExpectOk(TicketStatus status) throws Exception {
        mockMvc.perform(patch("/api/v1/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"" + status + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(status.name()));
    }
}
