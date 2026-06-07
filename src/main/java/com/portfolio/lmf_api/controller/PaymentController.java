package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.RequestPaymentDTO;
import com.portfolio.lmf_api.dto.ResponsePaymentDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired private PaymentService service;

    @GetMapping("/{courtName}")
    public ResponseEntity<List<ResponsePaymentDTO>> getByCourtName(@PathVariable String courtName) {
        try {
            return ResponseEntity.ok(service.getByCourtName(courtName));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPayment(@RequestBody RequestPaymentDTO request) {
        try {
            return ResponseEntity.ok(service.addPayment(request));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
