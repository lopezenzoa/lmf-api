package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.MatchDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface MatchService {
    MatchDTO addMatch(MatchDTO request);
    MatchDTO updateMatch(Long matchId, MatchDTO request);
    MatchDTO getById(Long id);
    List<MatchDTO> getByCourtName(String courtName);
    List<MatchDTO> getByDate(LocalDateTime date);
}
