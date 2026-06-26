package com.portfolio.lmf_api.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ResponsePaymentDTO {
    private Integer amount;
    private LocalDateTime timestamp;
    private MatchDTO match;
    private String courtName;
    private String qrCodeUrl;
}
