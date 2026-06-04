package com.portfolio.lmf_api.controller;

import com.portfolio.lmf_api.dto.RequestPaymentDTO;
import com.portfolio.lmf_api.dto.ResponsePaymentDTO;
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
        return ResponseEntity.ok(service.getByCourtName(courtName));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponsePaymentDTO> addPayment(@RequestBody RequestPaymentDTO request) {
        return ResponseEntity.ok(service.addPayment(request));
    }
}
