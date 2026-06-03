package com.portfolio.lmf_api.service.impl;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.model.Match;
import com.portfolio.lmf_api.repository.MatchRepository;
import com.portfolio.lmf_api.service.CourtService;
import com.portfolio.lmf_api.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {
    @Autowired private MatchRepository repository;
    @Autowired private CourtService courtService;

    @Override
    public MatchDTO addMatch(MatchDTO request) {
        /* Building the Entity */
        Match entity = new Match();

        entity.setDate(request.getDate());
        entity.setDivisionName(request.getDivisionName().trim().toUpperCase());
        entity.setHomeTeamName(request.getHomeTeamName().trim().toUpperCase());
        entity.setVisitTeamName(request.getVisitTeamName().trim().toUpperCase());
        entity.setPayments(new ArrayList<>());

        /* Searching the Court */

        /* Saving the Entity */
        Match savedEntity = repository.save(entity);

        /* Building the Response DTO */
        MatchDTO matchDTO = new MatchDTO();

        matchDTO.setDate(savedEntity.getDate());
        matchDTO.setDivisionName(savedEntity.getDivisionName());
        matchDTO.setHomeTeamName(savedEntity.getHomeTeamName());
        matchDTO.setVisitTeamName(savedEntity.getVisitTeamName());

        return matchDTO;
    }

    @Override
    public MatchDTO updateMatch(Long matchId, MatchDTO request) {
        /* Searching for coincidence with id */
        Optional<Match> matchOptional = repository.findById(matchId);

        if (matchOptional.isEmpty())
            return null;

        /* Building the Entity */
        Match entity = new Match();

        entity.setDate(request.getDate());
        entity.setDivisionName(request.getDivisionName().trim().toUpperCase());
        entity.setHomeTeamName(request.getHomeTeamName().trim().toUpperCase());
        entity.setVisitTeamName(request.getVisitTeamName().trim().toUpperCase());
        entity.setPayments(new ArrayList<>());

        /* Searching the Court */

        /* Updating the Entity */
        Match updatedEntity = repository.save(entity);

        /* Building the Response DTO */
        MatchDTO matchDTO = new MatchDTO();

        matchDTO.setDate(updatedEntity.getDate());
        matchDTO.setDivisionName(updatedEntity.getDivisionName());
        matchDTO.setHomeTeamName(updatedEntity.getHomeTeamName());
        matchDTO.setVisitTeamName(updatedEntity.getVisitTeamName());

        return matchDTO;
    }

    @Override
    public MatchDTO getById(Long id) {
        Optional<Match> matchOptional = repository.findById(id);

        if (matchOptional.isEmpty())
            return null;

        /* Building the Response DTO */
        MatchDTO matchDTO = new MatchDTO();
        Match match = matchOptional.get();

        matchDTO.setDate(match.getDate());
        matchDTO.setDivisionName(match.getDivisionName());
        matchDTO.setHomeTeamName(match.getHomeTeamName());
        matchDTO.setVisitTeamName(match.getVisitTeamName());

        return matchDTO;
    }

    @Override
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

            matchDTO.setDate(match.getDate());
            matchDTO.setDivisionName(match.getDivisionName());
            matchDTO.setHomeTeamName(match.getHomeTeamName());
            matchDTO.setVisitTeamName(match.getVisitTeamName());

            responseDTOList.add(matchDTO);
        }

        return responseDTOList;
    }

    @Override
    public List<MatchDTO> getByDate(LocalDateTime date) {
        List<Match> matches = repository.findByDate(date);

        /* Building the Response DTO list */
        List<MatchDTO> responseDTOList = new ArrayList<>();

        for (Match match : matches) {
            MatchDTO matchDTO = new MatchDTO();

            matchDTO.setDate(match.getDate());
            matchDTO.setDivisionName(match.getDivisionName());
            matchDTO.setHomeTeamName(match.getHomeTeamName());
            matchDTO.setVisitTeamName(match.getVisitTeamName());

            responseDTOList.add(matchDTO);
        }

        return responseDTOList;
    }
}
