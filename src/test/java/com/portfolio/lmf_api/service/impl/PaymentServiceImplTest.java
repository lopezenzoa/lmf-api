package com.portfolio.lmf_api.service.impl;

import com.portfolio.lmf_api.dto.*;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.repository.MatchRepository;
import com.portfolio.lmf_api.service.CourtService;
import com.portfolio.lmf_api.service.MatchService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PaymentServiceImplTest {
    @Autowired private PaymentServiceImpl service;
    private RequestPaymentDTO request;
    private ResponsePaymentDTO response;

    @Autowired private CourtService courtService;
    @Autowired private MatchService matchService;
    @Autowired private MatchRepository matchRepository;

    private MatchDTO matchResponse;
    private ResponseCourtDTO courtResponse;

    @BeforeEach
    void setUp() {
        request = buildRequest();

        courtResponse = courtService.addCourt(
                new RequestCourtDTO(
                        "Predio Kraglievich",
                        "Kraglievich 5950",
                        "Club Atletico El Cañon"
                )
        );

        matchResponse = matchService.addMatch(
                new MatchDTO(
                        "2026-06-27 14:00",
                        "1era Division",
                        "Club Atletico El Cañon",
                        "Club Atletico Kimberley"
                )
        );

        response = service.addPayment(request);
    }

    RequestPaymentDTO buildRequest() {
        return new RequestPaymentDTO(
                1000,
                1L
        );
    }

    @Test
    void whenAddingAPayment_thenReturnAResponsePaymentDTO() {
        assertEquals(request.getAmount(), response.getAmount());
        assertNotNull(response.getTimestamp());
        assertEquals(matchResponse, response.getMatch());
        assertEquals(courtResponse.getName(), response.getCourtName());
    }

    @Test
    void whenAddingAPaymentWithZeroOrNegativeAmount_thenThrowAnInvalidRequestFieldException() {
        matchRepository.deleteAll();

        RequestPaymentDTO invalidRequestWithZeroAmount = new RequestPaymentDTO(
                0,
                1L
        );

        RequestPaymentDTO invalidRequestWithNegativeAmount = new RequestPaymentDTO(
                -1000,
                1L
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.addPayment(invalidRequestWithZeroAmount)
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.addPayment(invalidRequestWithNegativeAmount)
        );
    }

    @Test
    void whenAddingAPaymentWithZeroOrNegativeMatchId_thenThrowAnInvalidRequestFieldException() {
        matchRepository.deleteAll();

        RequestPaymentDTO invalidRequestWithZeroMatchId = new RequestPaymentDTO(
                1000,
                0L
        );

        RequestPaymentDTO invalidRequestWithNegativeMatchId = new RequestPaymentDTO(
                1000,
                -1L
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.addPayment(invalidRequestWithZeroMatchId)
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.addPayment(invalidRequestWithNegativeMatchId)
        );
    }

    @Test
    void whenAddingWithAnNonExistentMatchId_thenThrowANotFoundException() {
        RequestPaymentDTO invalidRequest = new RequestPaymentDTO(
                1000,
                2L
        );

        assertThrows(
                NotFoundException.class,
                () -> service.addPayment(invalidRequest)
        );
    }

    @Test
    void whenGettingByCourtName_thenReturnAFilteredListOfResponsePaymentDTO() {
        List<ResponsePaymentDTO> payments = service.getByCourtName("Predio Kraglievich");
        List<ResponsePaymentDTO> expectedPayments = new ArrayList<>(Collections.singletonList(response));

        assertFalse(payments.isEmpty());
        assertFalse(expectedPayments.isEmpty());

        assertEquals(expectedPayments.getFirst().getAmount(), payments.getFirst().getAmount());
        assertEquals(expectedPayments.getFirst().getCourtName(), payments.getFirst().getCourtName());
        assertEquals(expectedPayments.getFirst().getMatch(), payments.getFirst().getMatch());
    }

    @Test
    void whenGettingByCourtNameWithNoPayments_thenReturnAnEmptyList() {
        matchRepository.deleteAll();

        List<ResponsePaymentDTO> payments = service.getByCourtName("Predio Kraglievich");
        assertTrue(payments.isEmpty());
    }

    @Test
    void whenGettingByNonExistentCourtName_thenThrowANotFoundException() {
        assertThrows(
                NotFoundException.class,
                () -> service.getByCourtName("Predio Kraglievich II")
        );
    }

    @Test
    void whenGettingByNullCourtName_thenThrowAnInvalidRequestFieldException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.getByCourtName(null)
        );
    }

    @Test
    void whenGettingByBlankCourtName_thenThrowAnInvalidRequestFieldException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.getByCourtName("")
        );
    }

    @Test
    void whenGettingByCourtName_thenExcludeAllOtherPayments() {
        courtService.addCourt(
                new RequestCourtDTO(
                        "Predio Kraglievich II",
                        "Kraglievich 5950 II",
                        "Club Atletico El Cañon II"
                )
        );

        matchService.addMatch(
                new MatchDTO(
                        "2026-06-27 14:00",
                        "1era Division",
                        "Club Atletico El Cañon II",
                        "Club Atletico Kimberley"
                )
        );

        service.addPayment(
                new RequestPaymentDTO(
                        1000,
                        2L
                )
        );

        List<ResponsePaymentDTO> payments = service.getByCourtName("Predio Kraglievich");
        List<ResponsePaymentDTO> otherPayments = service.getByCourtName("Predio Kraglievich II");

        assertNotEquals(payments, otherPayments);
    }

    @AfterEach
    void tearDown() {
        matchRepository.deleteAll();
    }
}