package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {
    @Autowired private MatchService service;

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDTO> getById(@PathVariable Long matchId) {
        MatchDTO serviceRes = service.getById(matchId);
        return (serviceRes != null) ? ResponseEntity.ok(serviceRes) : ResponseEntity.notFound().build();
    }

    @GetMapping("/courtName/{courtName}")
    public ResponseEntity<List<MatchDTO>> getByCourtName(@PathVariable String courtName) {
        return ResponseEntity.ok(service.getByCourtName(courtName));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<MatchDTO>> getByDate(@PathVariable String date) {
        return ResponseEntity.ok(service.getByDate(date));
    }

    @PostMapping("/add")
    public ResponseEntity<MatchDTO> addMatch(@RequestBody MatchDTO request) {
        return ResponseEntity.ok(service.addMatch(request));
    }

    @PutMapping("/update/{matchId}")
    public ResponseEntity<MatchDTO> updateMatch(@PathVariable Long matchId, @RequestBody MatchDTO request) {
        MatchDTO serviceRes = service.updateMatch(matchId, request);
        return (serviceRes != null) ? ResponseEntity.ok(serviceRes) : ResponseEntity.notFound().build();
    }


}
