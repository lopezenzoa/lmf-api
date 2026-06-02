package com.portfolio.lmf_api.service;

import com.portfolio.lmf_api.dto.RequestCourtDTO;
import com.portfolio.lmf_api.dto.ResponseCourtDTO;

import java.util.List;
import java.util.Optional;

public interface CourtService {
    ResponseCourtDTO addCourt(RequestCourtDTO request);
    ResponseCourtDTO updateCourt(String courtName, RequestCourtDTO request);
    List<ResponseCourtDTO> getAll();
    Optional<ResponseCourtDTO> getByName(String name);
}
