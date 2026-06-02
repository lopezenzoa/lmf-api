package com.portfolio.lmf_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponsePaymentDTO {
    private Integer amount;
    private LocalDateTime timestamp;
    private MatchDTO match;
    private String courtName;
}
