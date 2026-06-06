package com.portfolio.lmf_api.service.impl;

import com.portfolio.lmf_api.dto.RequestPaymentDTO;
import com.portfolio.lmf_api.dto.ResponsePaymentDTO;
import com.portfolio.lmf_api.exception.InvalidRequestFieldException;
import com.portfolio.lmf_api.exception.NotFoundException;
import com.portfolio.lmf_api.model.Court;
import com.portfolio.lmf_api.model.Match;
import com.portfolio.lmf_api.model.Payment;
import com.portfolio.lmf_api.repository.CourtRepository;
import com.portfolio.lmf_api.repository.MatchRepository;
import com.portfolio.lmf_api.repository.PaymentRepository;
import com.portfolio.lmf_api.service.MatchService;
import com.portfolio.lmf_api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired private PaymentRepository repository;
    @Autowired private MatchService matchService;
    @Autowired private MatchRepository matchRepository;
    @Autowired private CourtRepository courtRepository;

    @Override
    public ResponsePaymentDTO addPayment(RequestPaymentDTO request) throws InvalidRequestFieldException {
        /* VERIFYING REQUEST */
        if (request.getAmount() <= 0 || request.getMatchId() <= 0)
            throw new InvalidRequestFieldException("AMOUNT OR MATCH ID ARE NEGATIVE OR ZERO");

        /* BUILDING THE ENTITY */
        Payment entity = new Payment();

        entity.setAmount(request.getAmount());
        entity.setTimestamp(LocalDateTime.now());

        /* SEARCHING THE MATCH */
        Optional<Match> matchOpt = matchRepository.findById(request.getMatchId());

        if (matchOpt.isPresent())
            entity.setMatch(matchOpt.get());
        else
            throw new NotFoundException("MATCH WITH ID '" + request.getMatchId() + "' DOESN'T EXIST");

        /* SAVING THE ENTITY */
        Payment savedEntity = repository.save(entity);

        /* BUILDING THE RESPONSE */
        return mapToResponse(savedEntity);
    }

    @Override
    public List<ResponsePaymentDTO> getByCourtName(String courtName) {
        /* INITIAL FORMATTING */
        courtName = courtName.trim().toUpperCase();

        /* SEARCHING COURT */
        Optional<Court> courtOpt = courtRepository.findByName(courtName);

        if (courtOpt.isEmpty())
            throw new NotFoundException("COURT WITH NAME '" + courtName + "' DOESN'T EXIST");

        List<Payment> payments = repository.findAll();

        /* FILTERING */
        List<Payment> paymentsFiltered = new ArrayList<>();

        for (Payment payment : payments) {
            if (payment.getMatch().getCourt().getName().equals(courtName))
                paymentsFiltered.add(payment);
        }

        /* BUILDING THE RESPONSE LIST */
        List<ResponsePaymentDTO> responseList = new ArrayList<>();

        for (Payment payment : paymentsFiltered) {
            responseList.add(mapToResponse(payment));
        }

        return responseList;
    }

    public ResponsePaymentDTO mapToResponse(Payment payment) {
        ResponsePaymentDTO responsePaymentDTO = new ResponsePaymentDTO();

        responsePaymentDTO.setAmount(payment.getAmount());
        responsePaymentDTO.setTimestamp(payment.getTimestamp());
        responsePaymentDTO.setMatch(matchService.mapToResponse(payment.getMatch()));
        responsePaymentDTO.setCourtName(payment.getMatch().getCourt().getName());

        return responsePaymentDTO;
    }
}
