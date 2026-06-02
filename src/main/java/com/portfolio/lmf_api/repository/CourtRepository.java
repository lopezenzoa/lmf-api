package com.portfolio.lmf_api.repository;

import com.portfolio.lmf_api.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    Optional<Court> findByName(String name);
    Optional<Court> findByOwnerTeamName(String name);
}
