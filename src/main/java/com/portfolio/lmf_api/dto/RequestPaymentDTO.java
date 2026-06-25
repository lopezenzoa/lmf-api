package com.portfolio.lmf_api.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RequestPaymentDTO {
    private Integer amount;
    private Long matchId;
}
