package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.RequestCourtDTO;
import com.portfolio.lmf_api.dto.ResponseCourtDTO;
import com.portfolio.lmf_api.model.Court;
import com.portfolio.lmf_api.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourtService {
    @Autowired
    private CourtRepository repository;

    public ResponseCourtDTO addCourt(RequestCourtDTO request) {
        /* Building the Entity */
        Court court = new Court();
        court.setName(request.getName().trim().toUpperCase());
        court.setAddress(request.getAddress().trim().toUpperCase());
        court.setOwnerTeamName(request.getOwnerTeamName().trim().toUpperCase());
        court.setMatches(new ArrayList<>()); // By default, creating a court means no matches

        /* Saving the Entity */
        Court savedEntity = repository.save(court);

        /* Building the Response DTO */
        ResponseCourtDTO responseCourtDTO = new ResponseCourtDTO();

        responseCourtDTO.setName(savedEntity.getName());
        responseCourtDTO.setAddress(savedEntity.getAddress());
        responseCourtDTO.setOwnerTeamName(savedEntity.getOwnerTeamName());
        responseCourtDTO.setMatches(new ArrayList<>());

        return responseCourtDTO;
    }

    public ResponseCourtDTO updateCourt(String courtName, RequestCourtDTO request) {
        /* Searching for coincides with the name */
        Optional<Court> courtOptional = repository.findByName(courtName.trim().toUpperCase());

        if (courtOptional.isEmpty())
            return null;

        /* Building the Entity */
        Court court = courtOptional.get();

        court.setName(request.getName().trim().toUpperCase());
        court.setAddress(request.getAddress().trim().toUpperCase());
        court.setOwnerTeamName(request.getOwnerTeamName().trim().toUpperCase());
        court.setMatches(court.getMatches()); // By default, creating a court means no matches

        /* Updating the Entity */
        Court updatedEntity = repository.save(court);

        /* Building the Response DTO */
        ResponseCourtDTO responseCourtDTO = new ResponseCourtDTO();

        responseCourtDTO.setName(updatedEntity.getName());
        responseCourtDTO.setAddress(updatedEntity.getAddress());
        responseCourtDTO.setOwnerTeamName(updatedEntity.getOwnerTeamName());
        responseCourtDTO.setMatches(new ArrayList<>());

        return responseCourtDTO;
    }

    public List<ResponseCourtDTO> getAll() {
        List<Court> courts = repository.findAll();
        List<ResponseCourtDTO> responseCourtDTOS = new ArrayList<>();

        for (Court court : courts) {
            /* Building the response DTO */
            ResponseCourtDTO responseCourtDTO = new ResponseCourtDTO();

            responseCourtDTO.setName(court.getName());
            responseCourtDTO.setAddress(court.getAddress());
            responseCourtDTO.setOwnerTeamName(court.getOwnerTeamName());
            responseCourtDTO.setMatches(new ArrayList<>());

            responseCourtDTOS.add(responseCourtDTO);
        }

        return responseCourtDTOS;
    }

    public Optional<ResponseCourtDTO> getByName(String name) {
        Optional<Court> courtOptional = repository.findByName(name.trim().toUpperCase());

        if (courtOptional.isEmpty())
            return Optional.empty();

        /* Building the response DTO */
        Court court = courtOptional.get();
        ResponseCourtDTO responseCourtDTO = new ResponseCourtDTO();

        responseCourtDTO.setName(court.getName());
        responseCourtDTO.setAddress(court.getAddress());
        responseCourtDTO.setOwnerTeamName(court.getOwnerTeamName());
        responseCourtDTO.setMatches(new ArrayList<>());

        return Optional.of(responseCourtDTO);
    }
}
