package com.portfolio.lmf_api.repository;

import com.portfolio.lmf_api.model.Court;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourtRepositoryTest {
    @Autowired private CourtRepository underTest;

    @Test
    void willNotBeFoundByName() {
        /* GIVEN */
        String courtName = "CLUB ATLÉTICO EL CAÑON";
        /* WHEN */
        Optional<Court> result = underTest.findByName(courtName);
        /* THEN */
        assertThat(result).isEmpty();
    }

    @Test
    void willBeFoundByName() {
        /* GIVEN */
        String courtName = "PREDIO GRAGIELIVICH";

        Court court = new Court(
                null, courtName, "11 DE SEPTIEMBRE 4678", "CLUB ATLETICO EL CAÑON", new ArrayList<>()
        );

        underTest.save(court);

        /* WHEN */
        Optional<Court> result = underTest.findByName(courtName);

        /* THEN */
        assertThat(result).isPresent();
    }

    @Test
    void willNotBeFoundByOwnerTeamName() {
        /* GIVEN */
        String ownerTeamName = "CLUB ATLÉTICO EL CAÑON";
        /* WHEN */
        Optional<Court> result = underTest.findByOwnerTeamName(ownerTeamName);
        /* THEN */
        assertThat(result).isEmpty();
    }

    @Test
    void willBeFoundByOwnerTeamName() {
        /* GIVEN */
        String ownerTeamName = "CLUB ATLÉTICO EL CAÑON";

        Court court = new Court(
                null, "PREDIO KAGRIELIVICH", "11 DE SEPTIEMBRE 4678", ownerTeamName, new ArrayList<>()
        );

        underTest.save(court);

        /* WHEN */
        Optional<Court> result = underTest.findByOwnerTeamName(ownerTeamName);

        /* THEN */
        assertThat(result).isPresent();
    }
}