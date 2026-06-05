package com.portfolio.lmf_api.repository;

import com.portfolio.lmf_api.model.Court;
import com.portfolio.lmf_api.model.Match;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import javax.swing.text.html.Option;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MatchRepositoryTest {
    @Autowired private MatchRepository underTest;
    @Autowired private CourtRepository courtRepository;

    @Test
    void willNotBeFoundByDivisionName() {
        /* GIVEN */
        String divisionName = "1ERA DIVISION";

        /* WHEN */
        List<Match> result = underTest.findByDivisionName(divisionName);

        /* THEN */
        List<Match> expected = new ArrayList<>();

        assertThat(result).isNotIn(expected);
    }

    @Test
    void willBeFoundByDivisionName() {
        /* GIVEN */
        Court court = new Court(
                null, "PREDIO KRAGLIEVICH", "11 DE SEPTIEMBRE 4678", "CLUB ATLETICO EL CAÑON", new ArrayList<>()
        );

        Court courtSaved = courtRepository.save(court);

        String divisionName = "1ERA DIVISION";
        Match match = new Match(
                null,
                LocalDateTime.now(),
                divisionName,
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KEMBERLEY",
                courtSaved,
                new ArrayList<>()
        );

        underTest.save(match);

        /* WHEN */
        List<Match> result = underTest.findByDivisionName(divisionName);

        /* THEN */
        List<Match> expected = new ArrayList<>();
        expected.add(match);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void willNotBeFoundByHomeTeamName() {
        /* GIVEN */
        Court court = new Court(
                null, "PREDIO KRAGLIEVICH", "11 DE SEPTIEMBRE 4678", "CLUB ATLETICO EL CAÑON", new ArrayList<>()
        );

        Court courtSaved = courtRepository.save(court);

        String homeTeamName = "CLUB ATLETICO KIMBERLEY";

        Match match = new Match(
                null,
                LocalDateTime.now(),
                "1ERA DIVISION",
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KEMBERLEY",
                courtSaved,
                new ArrayList<>()
        );

        underTest.save(match);

        /* WHEN */
        Optional<Match> result = underTest.findByHomeTeamName(homeTeamName);

        /* THEN */
        assertThat(result).isEmpty();
    }

    @Test
    void willBeFoundByHomeTeamName() {
        /* GIVEN */
        Court court = new Court(
                null, "PREDIO KRAGLIEVICH", "11 DE SEPTIEMBRE 4678", "CLUB ATLETICO EL CAÑON", new ArrayList<>()
        );

        Court courtSaved = courtRepository.save(court);

        String homeTeamName = "CLUB ATLETICO EL CAÑON";

        Match match = new Match(
                null,
                LocalDateTime.now(),
                "1ERA DIVISION",
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KEMBERLEY",
                courtSaved,
                new ArrayList<>()
        );

        underTest.save(match);

        /* WHEN */
        Optional<Match> result = underTest.findByHomeTeamName(homeTeamName);

        /* THEN */
        assertThat(result).isPresent();
    }

    @Test
    void willNotBeFundByVisitTeamName() {
        /* GIVEN */
        Court court = new Court(
                null, "PREDIO KRAGLIEVICH", "11 DE SEPTIEMBRE 4678", "CLUB ATLETICO EL CAÑON", new ArrayList<>()
        );

        Court courtSaved = courtRepository.save(court);

        String visitTeamName = "CLUB ATLETICO MAR DEL PLATA";

        Match match = new Match(
                null,
                LocalDateTime.now(),
                "1ERA DIVISION",
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KIMBERLEY",
                courtSaved,
                new ArrayList<>()
        );

        underTest.save(match);

        /* WHEN */
        Optional<Match> result = underTest.findByVisitTeamName(visitTeamName);

        /* THEN */
        assertThat(result).isEmpty();
    }

    @Test
    void willNotBeFoundByDate() {
        /* GIVEN */
        Court court = new Court(
                null, "PREDIO KRAGLIEVICH", "11 DE SEPTIEMBRE 4678", "CLUB ATLETICO EL CAÑON", new ArrayList<>()
        );

        Court courtSaved = courtRepository.save(court);

        LocalDateTime date = LocalDateTime.of(2026, 6, 5, 11, 0);

        Match match = new Match(
                null,
                date,
                "1ERA DIVISION",
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KIMBERLEY",
                courtSaved,
                new ArrayList<>()
        );

        underTest.save(match);

        /* WHEN */
        List<Match> result = underTest.findByDate(LocalDateTime.of(2026, 6, 5, 12, 0));

        /* THEN */
        List<Match> expected = new ArrayList<>();
        expected.add(match);

        assertThat(result).isNotEqualTo(expected);
    }

    @Test
    void willBeFoundByDate() {
        /* GIVEN */
        Court court = new Court(
                null, "PREDIO KRAGLIEVICH", "11 DE SEPTIEMBRE 4678", "CLUB ATLETICO EL CAÑON", new ArrayList<>()
        );

        Court courtSaved = courtRepository.save(court);

        LocalDateTime date = LocalDateTime.of(2026, 6, 5, 11, 0);

        Match match = new Match(
                null,
                date,
                "1ERA DIVISION",
                "CLUB ATLETICO EL CAÑON",
                "CLUB ATLETICO KIMBERLEY",
                courtSaved,
                new ArrayList<>()
        );

        underTest.save(match);

        /* WHEN */
        List<Match> result = underTest.findByDate(date);

        /* THEN */
        List<Match> expected = new ArrayList<>();
        expected.add(match);

        assertThat(result).isEqualTo(expected);
    }
}