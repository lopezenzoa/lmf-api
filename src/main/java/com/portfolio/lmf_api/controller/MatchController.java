package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.exception.UniquenessViolationException;
import com.portfolio.lmf_api.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {
    @Autowired private MatchService service;

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDTO> getById(@PathVariable Long matchId) {
        try {
            return ResponseEntity.ok(service.getById(matchId));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/courtName/{courtName}")
    public ResponseEntity<List<MatchDTO>> getByCourtName(@PathVariable String courtName) {
        try {
            return ResponseEntity.ok(service.getByCourtName(courtName));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<MatchDTO>> getByDate(@PathVariable String date) {
        try {
            return ResponseEntity.ok(service.getByDate(date));
        } catch (DateTimeParseException | InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMatch(@RequestBody MatchDTO request) {
        try {
            return ResponseEntity.ok(service.addMatch(request));
        } catch (InvalidRequestFieldException | UniquenessViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{matchId}")
    public ResponseEntity<?> updateMatch(@PathVariable Long matchId, @RequestBody MatchDTO request) {
        try {
            return ResponseEntity.ok(service.updateMatch(matchId, request));
        } catch (InvalidRequestFieldException | UniquenessViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
