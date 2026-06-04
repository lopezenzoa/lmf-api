package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.model.Match;
import com.portfolio.lmf_api.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private MatchRepository repository;
    @Autowired private CourtService courtService;

    public MatchDTO addMatch(MatchDTO request) {
        /* Building the Entity */
        Match entity = new Match();

        entity.setDate(parseDate(request.getDate()));
        entity.setDivisionName(request.getDivisionName().trim().toUpperCase());
        entity.setHomeTeamName(request.getHomeTeamName().trim().toUpperCase());
        entity.setVisitTeamName(request.getVisitTeamName().trim().toUpperCase());
        entity.setPayments(new ArrayList<>());

        /* Searching the Court */

        /* Saving the Entity */
        Match savedEntity = repository.save(entity);

        /* Building the Response DTO */
        MatchDTO matchDTO = new MatchDTO();

        matchDTO.setDate(savedEntity.getDate().toString());
        matchDTO.setDivisionName(savedEntity.getDivisionName());
        matchDTO.setHomeTeamName(savedEntity.getHomeTeamName());
        matchDTO.setVisitTeamName(savedEntity.getVisitTeamName());

        return matchDTO;
    }

    public MatchDTO updateMatch(Long matchId, MatchDTO request) {
        /* Searching for coincidence with id */
        Optional<Match> matchOptional = repository.findById(matchId);

        if (matchOptional.isEmpty())
            return null;

        /* Building the Entity */
        Match entity = new Match();

        entity.setDate(parseDate(request.getDate()));
        entity.setDivisionName(request.getDivisionName().trim().toUpperCase());
        entity.setHomeTeamName(request.getHomeTeamName().trim().toUpperCase());
        entity.setVisitTeamName(request.getVisitTeamName().trim().toUpperCase());
        entity.setPayments(new ArrayList<>());

        /* Searching the Court */

        /* Updating the Entity */
        Match updatedEntity = repository.save(entity);

        /* Building the Response DTO */
        MatchDTO matchDTO = new MatchDTO();

        matchDTO.setDate(updatedEntity.getDate().toString());
        matchDTO.setDivisionName(updatedEntity.getDivisionName());
        matchDTO.setHomeTeamName(updatedEntity.getHomeTeamName());
        matchDTO.setVisitTeamName(updatedEntity.getVisitTeamName());

        return matchDTO;
    }

    public MatchDTO getById(Long id) {
        Optional<Match> matchOptional = repository.findById(id);

        if (matchOptional.isEmpty())
            return null;

        /* Building the Response DTO */
        MatchDTO matchDTO = new MatchDTO();
        Match match = matchOptional.get();

        matchDTO.setDate(match.getDate().toString());
        matchDTO.setDivisionName(match.getDivisionName());
        matchDTO.setHomeTeamName(match.getHomeTeamName());
        matchDTO.setVisitTeamName(match.getVisitTeamName());

        return matchDTO;
    }

    public List<MatchDTO> getByCourtName(String courtName) {
        List<Match> matches = repository.findAll();

        /* Filtering with Court */
        List<Match> matchesFiltered = new ArrayList<>();

        for (Match match : matches) {
            if (match.getCourt().getName().equals(courtName.trim().toUpperCase()))
                matchesFiltered.add(match);
        }

        /* Building the Response DTO list */
        List<MatchDTO> responseDTOList = new ArrayList<>();

        for (Match match : matchesFiltered) {
            MatchDTO matchDTO = new MatchDTO();

            matchDTO.setDate(match.getDate().toString());
            matchDTO.setDivisionName(match.getDivisionName());
            matchDTO.setHomeTeamName(match.getHomeTeamName());
            matchDTO.setVisitTeamName(match.getVisitTeamName());

            responseDTOList.add(matchDTO);
        }

        return responseDTOList;
    }

    public List<MatchDTO> getByDate(LocalDateTime date) {
        List<Match> matches = repository.findByDate(date);

        /* Building the Response DTO list */
        List<MatchDTO> responseDTOList = new ArrayList<>();

        for (Match match : matches) {
            MatchDTO matchDTO = new MatchDTO();

            matchDTO.setDate(match.getDate().toString());
            matchDTO.setDivisionName(match.getDivisionName());
            matchDTO.setHomeTeamName(match.getHomeTeamName());
            matchDTO.setVisitTeamName(match.getVisitTeamName());

            responseDTOList.add(matchDTO);
        }

        return responseDTOList;
    }

    private LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
