package com.portfolio.lmf_api.repository;

import com.portfolio.lmf_api.model.Court;
import com.portfolio.lmf_api.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MatchRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MatchRepository repository;
    @Autowired
    private CourtRepository courtRepository;

    private Match match;

    @BeforeEach
    void setUp() {
        Court court = buildCourt();
        entityManager.persist(court);

        match = buildMatch();
        entityManager.persist(match);

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

    Match buildMatch() {
        match = new Match(
                null,
                LocalDateTime.of(2026, 6, 21, 14, 0),
                "1era División",
                "Club Atletico El Cañon",
                "Club Atletico Kimberley",
                null,
                new ArrayList<>()
        );

        Optional<Court> court = courtRepository.findByOwnerTeamName(match.getHomeTeamName());
        court.ifPresent(value -> match.setCourt(value));

        return match;
    }

    @Test
    void whenFindByDivisionName_thenReturnAListOfMatches() {
        /* GIVEN */
        /* WHEN */
        List<Match> matches = repository.findByDivisionName("1era División");

        List<Match> expected = new ArrayList<>(Collections.singletonList(match));

        /* THEN */
        assertEquals(expected, matches);
        assertEquals(expected.size(), matches.size());
        assertEquals(1, matches.size());
    }

    @Test
    void whenFindByHomeTeamName_thenReturnMatch() {
        /* GIVEN */
        /* WHEN */
        Optional<Match> optionalMatch = repository.findByHomeTeamName("Club Atletico El Cañon");

        /* THEN */
        assertTrue(optionalMatch.isPresent());
        assertEquals(match.getHomeTeamName(), optionalMatch.get().getHomeTeamName());
        assertEquals(match, optionalMatch.get());
    }

    @Test
    void whenFindByVisitTeamName_thenReturnMatch() {
        /* GIVEN */
        /* WHEN */
        Optional<Match> optionalMatch = repository.findByVisitTeamName("Club Atletico Kimberley");

        /* THEN */
        assertTrue(optionalMatch.isPresent());
        assertEquals(match.getVisitTeamName(), optionalMatch.get().getVisitTeamName());
        assertEquals(match, optionalMatch.get());
    }

    @Test
    void findByDate() {
        /* GIVEN */
        /* WHEN */
        List<Match> matches = repository.findByDate(match.getDate());

        List<Match> expected = new ArrayList<>(Collections.singletonList(match));

        /* THEN */
        assertEquals(expected, matches);
        assertEquals(expected.size(), matches.size());
        assertEquals(1, matches.size());
    }
}