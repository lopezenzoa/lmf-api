package com.portfolio.lmf_api.repository;

import com.portfolio.lmf_api.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByDivisionName(String name);
    Optional<Match> findByHomeTeamName(String name);
    Optional<Match> findByVisitTeamName(String name);
    List<Match> findByDate(LocalDateTime date);
}
