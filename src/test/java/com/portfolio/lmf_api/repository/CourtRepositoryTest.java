package com.portfolio.lmf_api.repository;

import com.portfolio.lmf_api.model.Court;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CourtRepositoryTest {
    @Autowired private TestEntityManager entityManager;
    @Autowired private CourtRepository repository;
    private Court court;

    @BeforeEach
    void setUp() {
        court = buildCourt();

        entityManager.persist(court);
        entityManager.flush();
    }

    Court buildCourt() {
        return new Court(
                null,
                "Predio Kraglievich",
                "Kraglievich 5950",
                "Club Atletico El Cañon",
                new ArrayList<>()
        );
    }

    @Test
    void whenFindByName_thenReturnCourtOptional() {
        /* GIVEN */

        /* WHEN */
        Optional<Court> found = repository.findByName(court.getName());

        /* THEN */
        assertTrue(found.isPresent());
        assertEquals(court, found.get());
    }

    @Test
    void whenFindByOwnerTeamName_thenReturnCourtOptional() {
        /* GIVEN */

        /* WHEN */
        Optional<Court> found = repository.findByOwnerTeamName(court.getOwnerTeamName());

        /* THEN */
        assertTrue(found.isPresent());
        assertEquals(court, found.get());
    }
}