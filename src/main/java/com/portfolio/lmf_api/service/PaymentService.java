package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.RequestPaymentDTO;
import com.portfolio.lmf_api.dto.ResponsePaymentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    ResponsePaymentDTO addPayment(RequestPaymentDTO request);
    List<ResponsePaymentDTO> getByCourtName(String courtName);
}
