package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.RequestCourtDTO;
import com.portfolio.lmf_api.dto.ResponseCourtDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.exception.UniquenessViolationException;
import com.portfolio.lmf_api.service.CourtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/court")
public class CourtController {
    @Autowired private CourtService service;

    @GetMapping("/all")
    public ResponseEntity<List<ResponseCourtDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{courtName}")
    public ResponseEntity<ResponseCourtDTO> getByName(@PathVariable String courtName) {
        try {
            ResponseCourtDTO serviceRes = service.getByName(courtName);
            return ResponseEntity.ok(serviceRes);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourt(@RequestBody RequestCourtDTO request) {
        try {
            return ResponseEntity.ok(service.addCourt(request));
        } catch (UniquenessViolationException | InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{courtName}")
    public ResponseEntity<?> update(
            @PathVariable String courtName,
            @RequestBody RequestCourtDTO request
    ) {
        try {
            return ResponseEntity.ok(service.updateCourt(courtName, request));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestFieldException | UniquenessViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
