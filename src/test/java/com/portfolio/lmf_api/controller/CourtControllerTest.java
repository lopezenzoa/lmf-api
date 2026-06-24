package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.RequestCourtDTO;
import com.portfolio.lmf_api.dto.ResponseCourtDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.exception.UniquenessViolationException;
import com.portfolio.lmf_api.service.CourtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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

@WebMvcTest(CourtController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CourtControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private CourtService service;
    @Autowired private ObjectMapper objectMapper;
    private RequestCourtDTO request;

    @BeforeEach
    void setUp() {
        request = buildRequest();
    }

    RequestCourtDTO buildRequest() {
        return new RequestCourtDTO(
                "Predio Kraglievich",
                "Kraglievich 5950",
                "Club Atletico El Cañon"
        );
    }

    @Test
    void whenGettingAllCourts_thenReturn200WithList() throws Exception {
        List<ResponseCourtDTO> expected = List.of(
                new ResponseCourtDTO(
                        "PREDIO KRAGLIEVICH",
                        "KRAGLIEVICH 5950",
                        "CLUB ATLETICO EL CAÑON",
                        new ArrayList<>()
                ),
                new ResponseCourtDTO(
                        "PREDIO KRAGLIEVICH II",
                        "KRAGLIEVICH 5950 II",
                        "CLUB ATLETICO EL CAÑON II",
                        new ArrayList<>()
                )
        );

        when(service.getAll()).thenReturn(expected);

        mockMvc.perform(get("/api/court/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("PREDIO KRAGLIEVICH"))
                .andExpect(jsonPath("$[1].name").value("PREDIO KRAGLIEVICH II"));
    }

    @Test
    void whenGettingByName_thenReturn200WithBody() throws Exception {
        ResponseCourtDTO expectedResponse = new ResponseCourtDTO(
                "PREDIO KRAGLIEVICH",
                "KRAGLIEVICH 5950",
                "CLUB ATLETICO EL CAÑON",
                new ArrayList<>()
        );

        String validCourtName = "Predio Kraglievich";

        when(service.getByName(validCourtName)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/court/" + validCourtName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()));
    }

    @Test
    void whenGettingByNonExistentName_thenReturn404WithNoBody() throws Exception {
        String nonExistentCourtName = "Pedio Kraglievich III";

        when(service.getByName(nonExistentCourtName)).thenThrow(new NotFoundException(""));

        mockMvc.perform(get("/api/court/" + nonExistentCourtName))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAddingACourt_thenReturn200WithResponseCourtDTO() throws Exception {
        ResponseCourtDTO expectedResponse = new ResponseCourtDTO(
                "PREDIO KRAGLIEVICH",
                "KRAGLIEVICH 5950",
                "CLUB ATLETICO EL CAÑON",
                new ArrayList<>()
        );

        when(service.addCourt(request)).thenReturn(expectedResponse);

        mockMvc.perform(
                        post("/api/court/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()));
    }

    @Test
    void whenAddingADuplicatedCourt_thenReturn400WithErrorMessageInBody() throws Exception {
        when(service.addCourt(request)).thenThrow(new UniquenessViolationException("COURT ALREADY ADDED"));

        mockMvc.perform(
                        post("/api/court/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("COURT ALREADY ADDED"));
    }

    @Test
    void whenAddingAnInvalidCourt_thenReturn400WithErrorMessageInBody() throws Exception {
        when(service.addCourt(request)).thenThrow(new InvalidRequestFieldException("COURT NAME CAN'T BE NULL"));

        mockMvc.perform(
                        post("/api/court/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("COURT NAME CAN'T BE NULL"));
    }

    @Test
    void whenUpdatingACourt_thenReturn200WithResponseCourtDTO() throws Exception {
        RequestCourtDTO updatedRequest = new RequestCourtDTO(
                "PREDIO KRAGLIEVICH II",
                "KRAGLIEVICH 5950 II",
                "CLUB ATLETICO EL CAÑON"
        );

        ResponseCourtDTO expectedUpdatedResponse = new ResponseCourtDTO(
                "PREDIO KRAGLIEVICH II",
                "KRAGLIEVICH 5950 II",
                "CLUB ATLETICO EL CAÑON",
                new ArrayList<>()
        );

        when(service.updateCourt(request.getName(), updatedRequest)).thenReturn(expectedUpdatedResponse);

        mockMvc.perform(
                        put("/api/court/update/" + request.getName())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedUpdatedResponse.getName()));
    }

    @Test
    void whenUpdatingByNonExistentName_thenReturn404WithNoBody() throws Exception {
        String nonExistentCourtName = "Pedio Kraglievich III";

        when(service.updateCourt(nonExistentCourtName, request))
                .thenThrow(new NotFoundException("COURT WITH NAME '" + nonExistentCourtName.toUpperCase() + "'"));

        mockMvc.perform(
                        put("/api/court/update/" + nonExistentCourtName)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdatingADuplicatedCourt_thenReturn400WithErrorMessageInBody() throws Exception {
        when(service.updateCourt(request.getName(), request))
                .thenThrow(new UniquenessViolationException("COURT ALREADY ADDED"));

        mockMvc.perform(
                        put("/api/court/update/" + request.getName())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("COURT ALREADY ADDED"));
    }

    @Test
    void whenUpdatingAnInvalidCourt_thenReturn400WithErrorMessageInBody() throws Exception {
        RequestCourtDTO updatedRequest = new RequestCourtDTO(null, null, null);

        when(service.updateCourt(request.getName(), updatedRequest))
                .thenThrow(new InvalidRequestFieldException("COURT NAME CAN'T BE NULL"));

        mockMvc.perform(
                        put("/api/court/update/" + request.getName())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("COURT NAME CAN'T BE NULL"));
    }
}