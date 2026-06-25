package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.RequestCourtDTO;
import com.portfolio.lmf_api.dto.ResponseCourtDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.exception.UniquenessViolationException;
import com.portfolio.lmf_api.repository.CourtRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CourtServiceTest {
    @Autowired private CourtRepository repository;
    @Autowired private CourtService service;
    private RequestCourtDTO requestDTO;
    private ResponseCourtDTO response;

    @BeforeEach
    void setUp() {
        requestDTO = buildRequest();
        response = service.addCourt(requestDTO);
    }

    RequestCourtDTO buildRequest() {
        return new RequestCourtDTO(
                "Predio Kraglievich",
                "Kraglievich 5950",
                "Club Atletico El Cañon"
        );
    }

    @Test
    void whenAddingACourt_thenReturnResponseCourtDTO() {
        assertEquals(response.getName(), requestDTO.getName());
        assertEquals(response.getAddress(), requestDTO.getAddress());
        assertEquals(response.getOwnerTeamName(), requestDTO.getOwnerTeamName());
    }

    @Test
    void whenAddingWithDuplicateName_thenThrowUniquenessViolationException() {
        assertThrows(
                UniquenessViolationException.class,
                () -> service.addCourt(requestDTO)
        );
    }

    @Test
    void whenAddingNullValue_thenThrowInvalidRequestFieldException() {
        RequestCourtDTO blankRequestDTO = new RequestCourtDTO(
                null,
                null,
                null
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.addCourt(blankRequestDTO)
        );
    }

    @Test
    void whenUpdatingACourt_thenReturnAnUpdatedResponseCourtDTO() {
        RequestCourtDTO updatedRequestDTO = new RequestCourtDTO(
                "Predio Kraglievich II",
                "Kraglievich 5950",
                "Club Atletico El Cañon"
        );

        ResponseCourtDTO updatedResponse = service.updateCourt(response.getName(), updatedRequestDTO);

        assertEquals(updatedRequestDTO.getName(), updatedResponse.getName());
        assertEquals(updatedRequestDTO.getAddress(), updatedResponse.getAddress());
        assertEquals(updatedRequestDTO.getOwnerTeamName(), updatedResponse.getOwnerTeamName());
    }

    @Test
    void whenUpdatingByNonExistentName_thenThrowNotFoundException() {
        RequestCourtDTO updatedRequestDTO = new RequestCourtDTO(
                "Predio Kraglievich II",
                "Kraglievich 5950",
                "Club Atletico El Cañon"
        );

        assertThrows(
                NotFoundException.class,
                () -> service.updateCourt(response.getName() + " II", updatedRequestDTO)
        );
    }

    @Test
    void whenUpdatingByNullName_thenThrowInvalidRequestFieldException() {
        RequestCourtDTO updatedRequestDTO = new RequestCourtDTO(
                null,
                "Kraglievich 5950",
                "Club Atletico El Cañon"
        );
        
        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.updateCourt(null, updatedRequestDTO)
        );
    }

    @Test
    void whenUpdatingWithAnExistentName_thenThrowUniquenessViolationException() {
        RequestCourtDTO existentRequestDTO = new RequestCourtDTO(
                "Predio Kraglievich II",
                "Kraglievich 5950 II",
                "Club Atletico El Cañon II"
        );

        service.addCourt(existentRequestDTO);

        RequestCourtDTO updatedRequestDTO = new RequestCourtDTO(
                "Predio Kraglievich II",
                "Kraglievich 5950 II",
                "Club Atletico El Cañon II"
        );

        assertThrows(
                UniquenessViolationException.class,
                () -> service.updateCourt(response.getName(), updatedRequestDTO)
        );
    }

    @Test
    void whenGettingAllCourts_thenReturnAListOfResponseCourtDTO() {
        List<ResponseCourtDTO> courts = service.getAll();
        
        assertFalse(courts.isEmpty());
        assertTrue(courts.contains(response));
    }

    @Test
    void whenGettingAllForEmptyRepository_thenReturnAnEmptyList() {
        repository.deleteAll();
        List<ResponseCourtDTO> courts = service.getAll();

        assertTrue(courts.isEmpty());
    }

    @Test
    void whenGettingByName_thenReturnACourt() {
        ResponseCourtDTO court = service.getByName("Predio Kraglievich");
        assertEquals(response.getName(), court.getName());
    }

    @Test
    void whenGettingByNonExistentName_thenThrowNotFoundException() {
        String nonExistentName = "Predio Kraglievich III";
        
        assertThrows(
                NotFoundException.class,
                () -> service.getByName(nonExistentName)
        );
    }

    @Test
    void whenGettingByNullName_thenThrowInvalidRequestFieldException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.getByName(null)
        );
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}