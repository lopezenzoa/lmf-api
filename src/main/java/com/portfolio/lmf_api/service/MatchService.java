package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.exception.UniquenessViolationException;
import com.portfolio.lmf_api.model.Court;
import com.portfolio.lmf_api.model.Match;
import com.portfolio.lmf_api.repository.CourtRepository;
import com.portfolio.lmf_api.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private MatchRepository repository;
    @Autowired private CourtRepository courtRepository;

    public MatchDTO addMatch(MatchDTO request) throws InvalidRequestFieldException, UniquenessViolationException, NotFoundException, DateTimeParseException {
        if (
                request.getDate() == null
                        || request.getDivisionName() == null
                        || request.getHomeTeamName() == null
                        || request.getVisitTeamName() == null
        )
            throw new InvalidRequestFieldException("INVALID REQUEST FIELDS");

        /* INITIAL FORMATTING */
        final LocalDateTime PARSED_DATE = parseDate(request.getDate());
        request.setDivisionName(request.getDivisionName().trim().toUpperCase());
        request.setHomeTeamName(request.getHomeTeamName().trim().toUpperCase());
        request.setVisitTeamName(request.getVisitTeamName().trim().toUpperCase());

        /* VERIFYING REQUEST */
        verifyEmptiness(request);

        /* VERIFYING UNIQUENESS */
        verifyUniqueness(request);

        /* BUILDING THE ENTITY */
        Match entity = new Match();

        entity.setDate(PARSED_DATE);
        entity.setDivisionName(request.getDivisionName());
        entity.setHomeTeamName(request.getHomeTeamName());
        entity.setVisitTeamName(request.getVisitTeamName());
        entity.setPayments(new ArrayList<>());

        /* SEARCHING THE COURT */
        Optional<Court> courtOpt = courtRepository.findByOwnerTeamName(entity.getHomeTeamName());

        if (courtOpt.isPresent())
            entity.setCourt(courtOpt.get());
        else
            throw new NotFoundException("COURT WITH OWNER TEAM NAME '" + request.getHomeTeamName() + "' DOESN'T EXIST");

        /* SAVING THE ENTITY */
        Match savedEntity = repository.save(entity);

        /* BUILDING THE RESPONSE */
        return mapToResponse(savedEntity);
    }

    public MatchDTO updateMatch(Long matchId, MatchDTO request) throws InvalidRequestFieldException, NotFoundException, UniquenessViolationException, DateTimeParseException {
        if (
                request.getDate() == null
                || request.getDivisionName() == null
                || request.getHomeTeamName() == null
                || request.getVisitTeamName() == null
                || matchId == null
        )
            throw new InvalidRequestFieldException("INVALID REQUEST FIELDS");

        /* INITIAL FORMATTING */
        final LocalDateTime PARSED_DATE = parseDate(request.getDate());
        request.setDivisionName(request.getDivisionName().trim().toUpperCase());
        request.setHomeTeamName(request.getHomeTeamName().trim().toUpperCase());
        request.setVisitTeamName(request.getVisitTeamName().trim().toUpperCase());

        /* SEARCHING THE CANDIDATE FOR UPDATING */
        Optional<Match> matchOptional = repository.findById(matchId);

        if (matchOptional.isEmpty())
            throw new NotFoundException("MATCH WITH ID '" + matchId + "' DOESN'T EXIST");

        /* VERIFYING REQUEST */
        verifyEmptiness(request);

        /* VERIFYING UNIQUENESS OF FIELDS (EXCLUDING THE ACTUAL CANDIDATE) */
        verifyUniqueness(matchId, request);

        /* BUILDING THE ENTITY */
        Match entity = matchOptional.get();

        entity.setDate(PARSED_DATE);
        entity.setDivisionName(request.getDivisionName());
        entity.setHomeTeamName(request.getHomeTeamName());
        entity.setVisitTeamName(request.getVisitTeamName());

        /* SEARCHING THE COURT */
        Optional<Court> courtOpt = courtRepository.findByOwnerTeamName(request.getHomeTeamName());

        if (courtOpt.isPresent())
            entity.setCourt(courtOpt.get());
        else
            throw new NotFoundException("COURT WITH OWNER TEAM NAME '" + request.getHomeTeamName() + "' DOESN'T EXIST");

        /* UPDATING THE ENTITY */
        Match updatedEntity = repository.save(entity);

        /* BUILDING THE RESPONSE */
        return mapToResponse(updatedEntity);
    }

    public MatchDTO getById(Long id) throws NotFoundException, InvalidRequestFieldException {
        if (id == null)
            throw new InvalidRequestFieldException("ID CAN'T BE NULL");

        Optional<Match> matchOptional = repository.findById(id);

        if (matchOptional.isEmpty())
            throw new NotFoundException("MATCH NOT FOUND");

        /* BUILDING THE RESPONSE */
        Match match = matchOptional.get();

        return mapToResponse(match);
    }

    public List<MatchDTO> getByCourtName(String courtName) throws NotFoundException, InvalidRequestFieldException {
        if (courtName == null)
            throw new InvalidRequestFieldException("COURT NAME CAN'T BE NULL");

        /* INITIAL FORMATTING */
        courtName = courtName.trim().toUpperCase();

        /* SEARCHING COURT */
        Optional<Court> courtOpt = courtRepository.findByName(courtName);

        if (courtOpt.isEmpty())
            throw new NotFoundException("COURT WITH NAME '" + courtName + "' DOESN'T EXIST");

        List<Match> matches = repository.findAll();

        /* FILTERING */
        List<Match> matchesFiltered = new ArrayList<>();

        for (Match match : matches) {
            if (match.getCourt().getName().equals(courtName))
                matchesFiltered.add(match);
        }

        /* BUILDING THE RESPONSE LIST */
        List<MatchDTO> responseList = new ArrayList<>();

        for (Match match : matchesFiltered) {
            responseList.add(mapToResponse(match));
        }

        return responseList;
    }

    public List<MatchDTO> getByDate(String date) throws DateTimeParseException, InvalidRequestFieldException {
        if (date == null)
            throw new InvalidRequestFieldException("DATE CAN'T BE NULL");

        final LocalDateTime PARSED_DATE = parseDate(date);

        List<Match> matches = repository.findByDate(PARSED_DATE);

        /* BUILDING THE RESPONSE LIST */
        List<MatchDTO> responseList = new ArrayList<>();

        for (Match match : matches) {
            responseList.add(mapToResponse(match));
        }

        return responseList;
    }

    private LocalDateTime parseDate(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

    public MatchDTO mapToResponse(Match match) {
        MatchDTO matchDTO = new MatchDTO();

        /* FORMATTING THE DATE */
        String[] splitDate = match.getDate().toString().split("T");
        String formatedDate = splitDate[0] + " " + splitDate[1];

        matchDTO.setDate(formatedDate);
        matchDTO.setDivisionName(match.getDivisionName());
        matchDTO.setHomeTeamName(match.getHomeTeamName());
        matchDTO.setVisitTeamName(match.getVisitTeamName());

        return matchDTO;
    }

    private void verifyEmptiness(MatchDTO request) throws InvalidRequestFieldException {
        if (request.getDate().isEmpty()
                || request.getDivisionName().isEmpty()
                || request.getHomeTeamName().isEmpty()
                || request.getVisitTeamName().isEmpty()
        )
            throw new InvalidRequestFieldException("ONE OR MORE FIELD ON REQUEST ARE EMPTY");
    }

    private void verifyUniqueness(MatchDTO request) throws UniquenessViolationException {
        List<Match> matches = repository.findAll();

        for (Match match : matches)
            if (match.getDate().equals(parseDate(request.getDate()))
                    && match.getDivisionName().equals(request.getDivisionName())
                    && match.getHomeTeamName().equals(request.getHomeTeamName())
                    && match.getVisitTeamName().equals(request.getVisitTeamName())
            )
                throw new UniquenessViolationException("MATCH ALREADY EXISTS");
    }

    private void verifyUniqueness(Long matchId, MatchDTO request) throws UniquenessViolationException {
        List<Match> matches = repository.findAll();

        List<Match> matchesFiltered = matches
                .stream()
                .filter(match -> !match.getId().equals(matchId))
                .toList();

        for (Match match : matchesFiltered)
            if (match.getDate().equals(parseDate(request.getDate()))
                    && match.getDivisionName().equals(request.getDivisionName())
                    && match.getHomeTeamName().equals(request.getHomeTeamName())
                    && match.getVisitTeamName().equals(request.getVisitTeamName())
            )
                throw new UniquenessViolationException("MATCH ALREADY EXISTS");
    }
}
