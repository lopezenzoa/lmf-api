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
public class MatchDTO {
    private String date;
    private String divisionName;
    private String homeTeamName;
    private String visitTeamName;
}
