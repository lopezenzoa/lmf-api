package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.exception.UniquenessViolationException;
import com.portfolio.lmf_api.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
@ExtendWith(MockitoExtension.class)
class MatchControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private MatchService service;
    @Autowired private ObjectMapper objectMapper;
    private MatchDTO request;

    @BeforeEach
    void setUp() {
        request = buildRequest();
    }

    MatchDTO buildRequest() {
        return new MatchDTO(
                "2026-06-24 14:00",
                "1er División",
                "Club Atletico El Cañon",
                "Club Atletico Kimberley"
        );
    }

    @Test
    void whenGettingById_thenReturn200WithMatchDTOBody() throws Exception {
        when(service.getById(1L)).thenReturn(request);

        mockMvc.perform(get("/api/match/" + 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homeTeamName").value(request.getHomeTeamName()));
    }

    @Test
    void whenGettingByNonExistentId_thenReturn404WithNoBody() throws Exception {
        when(service.getById(100L))
                .thenThrow(new NotFoundException("MATCH NOT FOUND"));

        mockMvc.perform(get("/api/match/" + 100L))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGettingByCourtName_thenReturn200WithListOfMatchDTO() throws Exception {
        List<MatchDTO> matches = List.of(
                buildRequest(),
                new MatchDTO(
                        "2026-06-24 15:00",
                        "5ta División",
                        "Club Atletico El Cañon",
                        "Club Atletico Kimberley"
                )
        );

        String courtName = "Predio Kraglievich";

        when(service.getByCourtName(courtName)).thenReturn(matches);

        mockMvc.perform(get("/api/match/courtName/" + courtName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].divisionName").value("1er División"))
                .andExpect(jsonPath("$[1].divisionName").value("5ta División"));
    }

    @Test
    void whenGettingByCourtNameWithNoMatches_thenReturn200WithEmptyList() throws Exception {
        List<MatchDTO> matches = new ArrayList<>();

        String courtName = "Predio Kraglievich";

        when(service.getByCourtName(courtName)).thenReturn(matches);

        mockMvc.perform(get("/api/match/courtName/" + courtName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void whenGettingByNonExistentCourtName_thenReturn404WithNoBody() throws Exception {
        String courtName = "Predio Kraglievich";

        when(service.getByCourtName(courtName))
                .thenThrow(new NotFoundException("COURT WITH NAME '" + courtName.toUpperCase() + "' DOESN'T EXIST"));

        mockMvc.perform(get("/api/match/courtName/" + courtName))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGettingByDate_thenReturn200WithListOfMatchDTO() throws Exception {
        List<MatchDTO> matches = List.of(
                buildRequest(),
                new MatchDTO(
                        "2026-06-24 15:00",
                        "5ta División",
                        "Club Atletico Kimberley",
                        "Club Atletico San Lorenzo"
                )
        );

        String date = "2026-06-24 15:00";

        when(service.getByDate(date)).thenReturn(matches);

        mockMvc.perform(get("/api/match/date/" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].divisionName").value("1er División"))
                .andExpect(jsonPath("$[1].divisionName").value("5ta División"));
    }

    @Test
    void whenGettingByDateWithNoMatches_thenReturn200WithEmptyList() throws Exception {
        List<MatchDTO> matches = new ArrayList<>();

        String date = "2026-06-24 15:00";

        when(service.getByDate(date)).thenReturn(matches);

        mockMvc.perform(get("/api/match/date/" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void whenGettingByInvalidDate_thenReturn400WithNoBody() throws Exception {
        String date = "2026-Jun-24";

        when(service.getByDate(date))
                .thenThrow(new InvalidRequestFieldException("DATE CAN'T BE NULL"));

        mockMvc.perform(get("/api/match/date/" + date))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddingAMatch_thenReturnA200WithMatchDTOBody() throws Exception {
        MatchDTO expectedResponse = new MatchDTO(
                "2026-06-24 14:00",
                "5TA DIVISION",
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KIMBERLEY"
        );

        when(service.addMatch(request)).thenReturn(expectedResponse);

        mockMvc.perform(
                        post("/api/match/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(expectedResponse.getDate()))
                .andExpect(jsonPath("$.divisionName").value(expectedResponse.getDivisionName()))
                .andExpect(jsonPath("$.homeTeamName").value(expectedResponse.getHomeTeamName()))
                .andExpect(jsonPath("$.visitTeamName").value(expectedResponse.getVisitTeamName()));
    }

    @Test
    void whenAddingDuplicatedMatch_thenReturn400WithErrorMessageInBody() throws Exception {
        when(service.addMatch(request))
                .thenThrow(new UniquenessViolationException("MATCH ALREADY ADDED"));

        mockMvc.perform(
                post("/api/match/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("MATCH ALREADY ADDED"));
    }

    @Test
    void whenAddingAnInvalidMatch_thenReturn400WithErrorMessageInBody() throws Exception {
        when(service.addMatch(request))
                .thenThrow(new InvalidRequestFieldException("INVALID REQUEST FIELDS"));

        mockMvc.perform(
                        post("/api/match/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("INVALID REQUEST FIELDS"));
    }

    @Test
    void whenAddingWithNoAssociatedCourtForHomeTeam_thenReturnA404WithNoBody() throws Exception {
        when(service.addMatch(request))
                .thenThrow(new NotFoundException("COURT WITH OWNER TEAM NAME '" + request.getHomeTeamName().toUpperCase() + "' DOESN'T EXIST"));

        mockMvc.perform(
                        post("/api/match/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdatingAMatch_thenReturnA200WithUpdatedMatchDTOBody() throws Exception {
        MatchDTO updatedRequest = new MatchDTO(
                "2026-06-24 16:00",
                "5TA DIVISION",
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KIMBERLEY"
        );

        MatchDTO expectedUpdatedResponse = new MatchDTO(
                "2026-06-24 16:00",
                "5TA DIVISION",
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KIMBERLEY"
        );

        when(service.updateMatch(1L, updatedRequest)).thenReturn(expectedUpdatedResponse);

        mockMvc.perform(
                        put("/api/match/update/" + 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(expectedUpdatedResponse.getDate()))
                .andExpect(jsonPath("$.divisionName").value(expectedUpdatedResponse.getDivisionName()))
                .andExpect(jsonPath("$.homeTeamName").value(expectedUpdatedResponse.getHomeTeamName()))
                .andExpect(jsonPath("$.visitTeamName").value(expectedUpdatedResponse.getVisitTeamName()));
    }

    @Test
    void whenUpdatingAMatchWithNonExistentId_thenReturnA404WithNoBody() throws Exception {
        when(service.updateMatch(100L, request))
                .thenThrow(new NotFoundException("MATCH WITH ID '" + 100L + "' DOESN'T EXIST"));

        mockMvc.perform(
                put("/api/match/update/" + 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdatingADuplicatedMatch_thenReturn400WithErrorMessageInBody() throws Exception {
        when(service.updateMatch(1L, request))
                .thenThrow(new UniquenessViolationException("MATCH ALREADY EXISTS"));

        mockMvc.perform(
                        put("/api/match/update/" + 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("MATCH ALREADY EXISTS"));
    }

    @Test
    void whenUpdatingAnInvalidMatch_thenReturn400WithErrorMessageInBody() throws Exception {
        MatchDTO updatedRequest = new MatchDTO(null, null, null, null);

        when(service.updateMatch(1L, updatedRequest))
                .thenThrow(new InvalidRequestFieldException("INVALID REQUEST FIELDS"));

        mockMvc.perform(
                        put("/api/match/update/" + 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("INVALID REQUEST FIELDS"));
    }

    @Test
    void whenUpdatingWithNoAssociatedCourtForHomeTeam_thenReturnA404WithNoBody() throws Exception {
        when(service.updateMatch(1L, request))
                .thenThrow(new NotFoundException("COURT WITH OWNER TEAM NAME '" + request.getHomeTeamName().toUpperCase() + "' DOESN'T EXIST"));

        mockMvc.perform(
                        put("/api/match/update/" + 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }
}