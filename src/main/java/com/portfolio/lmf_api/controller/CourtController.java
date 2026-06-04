package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.RequestCourtDTO;
import com.portfolio.lmf_api.dto.ResponseCourtDTO;
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
        Optional<ResponseCourtDTO> serviceRes = service.getByName(courtName);

        return serviceRes.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseCourtDTO> addCourt(@RequestBody RequestCourtDTO request) {
        return ResponseEntity.ok(service.addCourt(request));
    }

    @PutMapping("/update/{courtName}")
    public ResponseEntity<ResponseCourtDTO> update(
            @PathVariable String courtName,
            @RequestBody RequestCourtDTO request
    ) {
        ResponseCourtDTO serviceRes = service.updateCourt(courtName, request);
        return (serviceRes != null) ? ResponseEntity.ok(serviceRes) : ResponseEntity.badRequest().build();
    }
}
