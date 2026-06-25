package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.dto.RequestPaymentDTO;
import com.portfolio.lmf_api.dto.ResponseCourtDTO;
import com.portfolio.lmf_api.dto.ResponsePaymentDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.service.impl.PaymentServiceImpl;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private PaymentServiceImpl service;
    private RequestPaymentDTO request;

    @BeforeEach
    void setUp() {
        request = buildRequest();
    }

    RequestPaymentDTO buildRequest() {
        return new RequestPaymentDTO(
                1000,
                1L
        );
    }

    @Test
    void whenGettingByCourtName_thenReturn200WithListOfResponsePaymentDTO() throws Exception {
        List<ResponsePaymentDTO> expectedResponse = List.of(
                new ResponsePaymentDTO(
                        1000,
                        LocalDateTime.now(),
                        new MatchDTO(
                        "2026-06-24 14:00",
                        "1er División",
                        "Club Atletico El Cañon",
                        "Club Atletico Kimberley"
                        ),
                        "Predio Kraglievich"
                ),
                new ResponsePaymentDTO(
                        1000,
                        LocalDateTime.now(),
                        new MatchDTO(
                                "2026-06-24 14:00",
                                "1er División",
                                "Club Atletico El Cañon",
                                "Club Atletico Kimberley"
                        ),
                        "Predio Kraglievich"
                )
        );

        String courtName = "Predio Kraglievich";

        when(service.getByCourtName(courtName)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/payment/" + courtName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].courtName").value("Predio Kraglievich"))
                .andExpect(jsonPath("$[1].courtName").value("Predio Kraglievich"));
    }

    @Test
    void whenGettingByCourtNameWithNoPayments_thenReturn200WithEmptyList() throws Exception {
        List<ResponsePaymentDTO> expectedResponse = new ArrayList<>();

        String courtName = "Predio Kraglievich";

        when(service.getByCourtName(courtName)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/payment/" + courtName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void whenGettingByNonExistentCourtName_thenReturn404WithNoBody() throws Exception {
        String nonExistentCourtName = "Predio Kraglievich XI";

        when(service.getByCourtName(nonExistentCourtName))
                .thenThrow(new NotFoundException("COURT WITH NAME '" + nonExistentCourtName + "' DOESN'T EXIST"));

        mockMvc.perform(get("/api/payment/" + nonExistentCourtName))
                .andExpect(status().isNotFound());
    }


    @Test
    void whenAddingAPayment_thenReturnA200WithResponsePaymentDTOBody() throws Exception {
        ResponsePaymentDTO expectedResponse = new ResponsePaymentDTO(
                1000,
                LocalDateTime.now(),
                new MatchDTO(
                        "2026-06-24 14:00",
                        "1er División",
                        "Club Atletico El Cañon",
                        "Club Atletico Kimberley"
                ),
                "Predio Kraglievich"
        );

        when(service.addPayment(request)).thenReturn(expectedResponse);

        mockMvc.perform(
                post("/api/payment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(expectedResponse.getAmount()))
                .andExpect(jsonPath("$.courtName").value(expectedResponse.getCourtName()));
    }

    @Test
    void whenAddingAPaymentWithZeroOrNegativeAmount_thenReturnA400WithErrorMessageInBody() throws Exception {
        when(service.addPayment(request))
                .thenThrow(new InvalidRequestFieldException("AMOUNT CAN'T BE NEITHER NEGATIVE OR ZERO"));

        mockMvc.perform(
                        post("/api/payment/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("AMOUNT CAN'T BE NEITHER NEGATIVE OR ZERO"));
    }

    @Test
    void whenAddingAPaymentWithZeroOrNegativeMatchId_thenReturnA400WithErrorMessageInBody() throws Exception {
        request = new RequestPaymentDTO(-1000, 1L);

        when(service.addPayment(request))
                .thenThrow(new InvalidRequestFieldException("MATCH ID CAN'T BE NEITHER NEGATIVE OR ZERO"));

        mockMvc.perform(
                        post("/api/payment/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("MATCH ID CAN'T BE NEITHER NEGATIVE OR ZERO"));
    }

    @Test
    void whenAddingAPaymentWithNonExistentMatchId_thenReturnA404WithNoBody() throws Exception {
        when(service.addPayment(request))
                .thenThrow(new NotFoundException("MATCH WITH ID '" + request.getMatchId() + "' DOESN'T EXIST"));

        mockMvc.perform(
                        post("/api/payment/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }
}