package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.dto.RequestCourtDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.exception.UniquenessViolationException;
import com.portfolio.lmf_api.repository.CourtRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MatchServiceTest {
    @Autowired private MatchService service;
    @Autowired private CourtService courtService;
    @Autowired private CourtRepository courtRepository;
    private MatchDTO request;
    private MatchDTO response;

    @BeforeEach
    void setUp() {
        request = buildRequest();

        courtService.addCourt(
                new RequestCourtDTO(
                        "Predio Kraglievich",
                        "Kraglievich 5950",
                        "Club Atletico El Cañon"
                )
        );

        response = service.addMatch(request);
    }

    MatchDTO buildRequest() {
        return new MatchDTO(
                "2026-06-27 14:00",
                "1era Division",
                "Club Atletico El Cañon",
                "Club Atletico Kimberley"
        );
    }


    @Test
    void whenAddingAMatch_thenReturnAMatchDTO() {
        assertEquals(request.getDate(), response.getDate());
        assertEquals(request.getDivisionName(), response.getDivisionName());
        assertEquals(request.getHomeTeamName(), response.getHomeTeamName());
        assertEquals(request.getVisitTeamName(), response.getVisitTeamName());
    }

    @Test
    void whenAddingADuplicatedMatch_thenThrowAnUniquenessViolationException() {
        assertThrows(
                UniquenessViolationException.class,
                () -> service.addMatch(request)
        );
    }

    @Test
    void whenAddingWithInvalidFields_thenThrowAnInvalidRequestFieldException() {
        MatchDTO invalidRequest = new MatchDTO(null, null, null, null);

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.addMatch(invalidRequest)
        );
    }

    @Test
    void whenAddingWithNoAssociatedCourt_thenThrowNotFoundException() {
        courtRepository.deleteAll();

        assertThrows(
                NotFoundException.class,
                () -> service.addMatch(request)
        );
    }

    @Test
    void whenAddingWithInvalidDateFormat_thenThrowDateTimeParseException() {
        courtRepository.deleteAll(); // Avoiding UniquenessViolationException

        request.setDate("2026/06/27 14:00:00");

        assertThrows(
                DateTimeParseException.class,
                () -> service.addMatch(request)
        );
    }

    @Test
    void whenUpdatingAMatch_thenReturnAnUpdatedMatchDTO() {
        MatchDTO updatedRequest = new MatchDTO(
                "2026-06-27 15:00",
                "1era Division",
                "Club Atletico El Cañon",
                "Club Atletico Kimberley"
        );

        MatchDTO updatedResponse = service.updateMatch(1L, updatedRequest);

        assertEquals(updatedRequest.getDate(), updatedResponse.getDate());
        assertEquals(updatedRequest.getDivisionName(), updatedResponse.getDivisionName());
        assertEquals(updatedRequest.getHomeTeamName(), updatedResponse.getHomeTeamName());
        assertEquals(updatedRequest.getVisitTeamName(), updatedResponse.getVisitTeamName());
    }

    @Test
    void whenUpdatingWithNonExistentMatchId_thenThrowAnNotFoundException() {
        MatchDTO updatedRequest = new MatchDTO(
                "2026-06-27 15:00",
                "1era Division",
                "Club Atletico El Cañon",
                "Club Atletico Kimberley"
        );

        assertThrows(
                NotFoundException.class,
                () -> service.updateMatch(100L, updatedRequest)
        );
    }

    @Test
    void whenUpdatingWithInvalidFields_thenThrowAnInvalidRequestFieldException() {
        MatchDTO updatedRequest = new MatchDTO(null, null, null, null);

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.updateMatch(1L, updatedRequest)
        );
    }

    @Test
    void whenUpdatingADuplicatedMatch_thenThrowAnUniquenessViolationException() {
        MatchDTO existentRequest = new MatchDTO(
                "2026-06-27 15:00",
                "5ta Division",
                "Club Atletico El Cañon",
                "Club Atletico Kimberley"
        );

        service.addMatch(existentRequest);

        MatchDTO updatedRequest = new MatchDTO(
                "2026-06-27 15:00",
                "5ta Division",
                "Club Atletico El Cañon",
                "Club Atletico Kimberley"
        );

        assertThrows(
                UniquenessViolationException.class,
                () -> service.updateMatch(1L, updatedRequest)
        );
    }

    @Test
    void whenSelfUpdatingAMatch_thenReturnSameResponseMatchDTO() {
        MatchDTO updatedRequest = request;

        MatchDTO updatedResponse = service.updateMatch(1L, updatedRequest);

        assertEquals(updatedRequest.getDate(), updatedResponse.getDate());
        assertEquals(updatedRequest.getDivisionName(), updatedResponse.getDivisionName());
        assertEquals(updatedRequest.getHomeTeamName(), updatedResponse.getHomeTeamName());
        assertEquals(updatedRequest.getVisitTeamName(), updatedResponse.getVisitTeamName());
    }

    @Test
    void whenUpdatingWithNoAssociatedCourt_thenThrowNotFoundException() {
        MatchDTO updatedRequest = new MatchDTO(
                "2026-06-27 15:00",
                "5ta Division",
                "Club Atletico San Lorenzo",
                "Club Atletico Kimberley"
        );

        assertThrows(
                NotFoundException.class,
                () -> service.updateMatch(1L, updatedRequest)
        );
    }

    @Test
    void whenUpdatingWithInvalidDateFormat_thenThrowDateTimeParseException() {
        MatchDTO updatedRequest = new MatchDTO(
                "2026/06/27 14:00:00",
                "5ta Division",
                "Club Atletico San Lorenzo",
                "Club Atletico Kimberley"
        );

        assertThrows(
                DateTimeParseException.class,
                () -> service.updateMatch(1L, updatedRequest)
        );
    }

    @Test
    void whenGettingById_thenReturnAResponseMatchDTO() {
        MatchDTO match = service.getById(1L);
        assertEquals(request, match);
    }


    @Test
    void whenGettingByNonExistentId_thenThrowANotFoundException() {
        assertThrows(
                NotFoundException.class,
                () -> service.getById(199L)
        );
    }

    @Test
    void whenGettingByNullId_thenThrowANotFoundException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.getById(null)
        );
    }

    @Test
    void whenGettingByCourtName_thenReturnAFilteredListMatchDTO() {
        List<MatchDTO> matches = service.getByCourtName("predio kraglievich");
        List<MatchDTO> expectedMatches = new ArrayList<>(Collections.singletonList(response));

        assertEquals(expectedMatches, matches);
    }

    @Test
    void whenGettingByCourtNameWithNoMatches_thenReturnAnEmptyList() {
        courtService.addCourt(
                new RequestCourtDTO(
                        "Predio Kraglievich II",
                        "Kraglievich 5950 II",
                        "Club Atletico El Cañon II"
                )
        );

        List<MatchDTO> matches = service.getByCourtName("Predio Kraglievich II");

        assertTrue(matches.isEmpty());
    }

    @Test
    void whenGettingByNonExistentCourtName_thenThrowANotFoundException() {
        assertThrows(
                NotFoundException.class,
                () -> service.getByCourtName("Predio Kraglievich III")
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
    void whenGettingByDate_thenReturnAFilteredListMatchDTO() {
        List<MatchDTO> matches = service.getByDate("2026-06-27 14:00");
        List<MatchDTO> expectedMatches = new ArrayList<>(Collections.singletonList(response));

        assertEquals(expectedMatches, matches);
    }

    @Test
    void whenGettingByDateWithNoMatches_thenReturnAnEmptyList() {
        List<MatchDTO> matches = service.getByDate("2026-06-27 17:00");
        assertTrue(matches.isEmpty());
    }

    @Test
    void whenGettingByInvalidDate_thenThrowADateTimeParseException() {
        assertThrows(
                DateTimeParseException.class,
                () -> service.getByDate("2026/06/27 14:00:00")
        );
    }

    @Test
    void whenGettingByNullDate_thenThrowAnInvalidRequestFieldException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.getByDate(null)
        );
    }

    @AfterEach
    void tearDown() {
        courtRepository.deleteAll();
    }
}