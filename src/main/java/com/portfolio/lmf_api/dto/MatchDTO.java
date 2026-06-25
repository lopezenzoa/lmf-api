package com.portfolio.lmf_api.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MatchDTO {
    private String date;
    private String divisionName;
    private String homeTeamName;
    private String visitTeamName;
}
