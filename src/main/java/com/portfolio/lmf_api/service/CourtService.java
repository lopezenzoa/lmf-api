package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.RequestCourtDTO;
import com.portfolio.lmf_api.dto.ResponseCourtDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.exception.UniquenessViolationException;
import com.portfolio.lmf_api.model.Court;
import com.portfolio.lmf_api.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourtService {
    @Autowired
    private CourtRepository repository;

    public ResponseCourtDTO addCourt(RequestCourtDTO request) throws InvalidRequestFieldException, UniquenessViolationException {
        /* INITIAL FORMATTING */
        request.setName(request.getName().trim().toUpperCase());
        request.setAddress(request.getAddress().trim().toUpperCase());
        request.setOwnerTeamName(request.getOwnerTeamName().trim().toUpperCase());

        /* VERIFYING REQUEST */
        verifyEmptiness(request);

        /* VERIFYING UNIQUENESS ON FIELDS */
        verifyUniqueness(request);

        /* BUILDING THE ENTITY */
        Court court = new Court();

        court.setName(request.getName());
        court.setAddress(request.getAddress());
        court.setOwnerTeamName(request.getOwnerTeamName());
        court.setMatches(new ArrayList<>()); // By default, creating a court means no matches

        /* SAVING THE ENTITY */
        Court savedEntity = repository.save(court);

        /* BUILDING THE RESPONSE */
        ResponseCourtDTO responseCourtDTO = new ResponseCourtDTO();

        responseCourtDTO.setName(savedEntity.getName());
        responseCourtDTO.setAddress(savedEntity.getAddress());
        responseCourtDTO.setOwnerTeamName(savedEntity.getOwnerTeamName());
        responseCourtDTO.setMatches(new ArrayList<>());

        return responseCourtDTO;
    }

    public ResponseCourtDTO updateCourt(String courtName, RequestCourtDTO request) throws NotFoundException, InvalidRequestFieldException, UniquenessViolationException {
        /* INITIAL FORMATTING */
        courtName = courtName.trim().toUpperCase();

        request.setName(request.getName().trim().toUpperCase());
        request.setAddress(request.getAddress().trim().toUpperCase());
        request.setOwnerTeamName(request.getOwnerTeamName().trim().toUpperCase());

        /* SEARCHING THE CANDIDATE FOR UPDATING */
        Optional<Court> courtOptional = repository.findByName(courtName);

        if (courtOptional.isEmpty())
            throw new NotFoundException("COURT WITH NAME '" + courtName + "' DOESN'T EXIST");

        /* VERIFYING REQUEST */
        verifyEmptiness(request);

        /* VERIFYING UNIQUENESS OF FIELDS (EXCLUDING THE CANDIDATE) */
        verifyUniqueness(courtName, request);

        /* BUILDING THE ENTITY */
        Court court = courtOptional.get();

        court.setName(request.getName());
        court.setAddress(request.getAddress());
        court.setOwnerTeamName(request.getOwnerTeamName());
        court.setMatches(court.getMatches());

        /* UPDATING THE ENTITY */
        Court updatedEntity = repository.save(court);

        /* BUILDING THE RESPONSE */
        ResponseCourtDTO responseCourtDTO = new ResponseCourtDTO();

        responseCourtDTO.setName(updatedEntity.getName());
        responseCourtDTO.setAddress(updatedEntity.getAddress());
        responseCourtDTO.setOwnerTeamName(updatedEntity.getOwnerTeamName());

        return responseCourtDTO;
    }

    public List<ResponseCourtDTO> getAll() {
        List<Court> courts = repository.findAll();
        List<ResponseCourtDTO> responseCourtDTOS = new ArrayList<>();

        for (Court court : courts) {
            /* BUILDING THE RESPONSE */
            ResponseCourtDTO responseCourtDTO = new ResponseCourtDTO();

            responseCourtDTO.setName(court.getName());
            responseCourtDTO.setAddress(court.getAddress());
            responseCourtDTO.setOwnerTeamName(court.getOwnerTeamName());
            responseCourtDTO.setMatches(new ArrayList<>());

            responseCourtDTOS.add(responseCourtDTO);
        }

        return responseCourtDTOS;
    }

    public ResponseCourtDTO getByName(String name) throws NotFoundException {
        /* INITIAL FORMATTING */
        name = name.trim().toUpperCase();

        Optional<Court> courtOptional = repository.findByName(name);

        if (courtOptional.isEmpty())
            throw new NotFoundException("COURT WITH NAME '" + name + "' DOESN'T EXIST");

        /* BUILDING THE RESPONSE */
        Court court = courtOptional.get();
        ResponseCourtDTO responseCourtDTO = new ResponseCourtDTO();

        responseCourtDTO.setName(court.getName());
        responseCourtDTO.setAddress(court.getAddress());
        responseCourtDTO.setOwnerTeamName(court.getOwnerTeamName());
        responseCourtDTO.setMatches(new ArrayList<>());

        return responseCourtDTO;
    }

    private void verifyUniqueness(RequestCourtDTO request) throws UniquenessViolationException {
        List<Court> courts = repository.findAll();

        for (Court court : courts) {
            if (court.getName().equals(request.getName())
                    || court.getAddress().equals(request.getAddress())
                    || court.getOwnerTeamName().equals(request.getOwnerTeamName())
            )
                throw new UniquenessViolationException("COURT ALREADY ADDED");
        }
    }

    private void verifyUniqueness(String courtName, RequestCourtDTO request) throws UniquenessViolationException {
        List<Court> courts = repository.findAll();

        List<Court> filteredCourts = courts
                .stream()
                .filter(court -> !court.getName().equals(courtName))
                .toList();

        for (Court court : filteredCourts) {
            System.out.println(court);

            if (court.getName().equals(request.getName())
                    || court.getAddress().equals(request.getAddress())
                    || court.getOwnerTeamName().equals(request.getOwnerTeamName())
            )
                throw new UniquenessViolationException("COURT ALREADY ADDED");
        }
    }

    private void verifyEmptiness(RequestCourtDTO request) throws InvalidRequestFieldException {
        if (request.getName().isEmpty()
                || request.getAddress().isEmpty()
                || request.getOwnerTeamName().isEmpty()
        )
            throw new InvalidRequestFieldException("ONE OR MORE FIELD ON REQUEST ARE EMPTY");
    }
}
