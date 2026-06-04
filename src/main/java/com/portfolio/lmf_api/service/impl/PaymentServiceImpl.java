package com.portfolio.lmf_api.service.impl;

import com.portfolio.lmf_api.dto.MatchDTO;
import com.portfolio.lmf_api.dto.RequestPaymentDTO;
import com.portfolio.lmf_api.dto.ResponsePaymentDTO;
import com.portfolio.lmf_api.model.Match;
import com.portfolio.lmf_api.model.Payment;
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

    @Override
    public ResponsePaymentDTO addPayment(RequestPaymentDTO request) {
        /* Building the Entity */
        Payment entity = new Payment();

        entity.setAmount(request.getAmount());
        entity.setTimestamp(LocalDateTime.now());

        /* Searching the Match */
        Optional<Match> matchOpt = matchRepository.findById(request.getMatchId());
        matchOpt.ifPresent(entity::setMatch);

        /* Saving the Entity */
        Payment savedEntity = repository.save(entity);

        /* Building the response DTO */
        ResponsePaymentDTO responsePaymentDTO = new ResponsePaymentDTO();

        if (matchOpt.isPresent()) {
            responsePaymentDTO.setAmount(savedEntity.getAmount());
            responsePaymentDTO.setTimestamp(savedEntity.getTimestamp());
            responsePaymentDTO.setMatch(matchService.mapToDTO(matchOpt.get()));
            responsePaymentDTO.setCourtName(savedEntity.getMatch().getCourt().getName());
        }

        return responsePaymentDTO;
    }

    @Override
    public List<ResponsePaymentDTO> getByCourtName(String courtName) {
        List<Payment> payments = repository.findAll();

        /* Filtering with Court name */
        List<Payment> paymentsFiltered = new ArrayList<>();

        for (Payment payment : payments) {
            if (payment.getMatch().getCourt().getName().equals(courtName.trim().toUpperCase()))
                paymentsFiltered.add(payment);
        }

        /* Building the response DTO list */
        List<ResponsePaymentDTO> paymentDTOList = new ArrayList<>();

        for (Payment payment : paymentsFiltered) {
            /* Building the Match */
            MatchDTO matchDTO = new MatchDTO();

            matchDTO.setDate(payment.getMatch().getDate().toString());
            matchDTO.setDivisionName(payment.getMatch().getDivisionName());
            matchDTO.setHomeTeamName(payment.getMatch().getHomeTeamName());
            matchDTO.setVisitTeamName(payment.getMatch().getVisitTeamName());

            ResponsePaymentDTO responsePaymentDTO = new ResponsePaymentDTO();

            responsePaymentDTO.setAmount(payment.getAmount());
            responsePaymentDTO.setTimestamp(payment.getTimestamp());
            responsePaymentDTO.setMatch(matchDTO);
            responsePaymentDTO.setCourtName(payment.getMatch().getCourt().getName());

            paymentDTOList.add(responsePaymentDTO);
        }

        return paymentDTOList;
    }
}
