package com.portfolio.lmf_api.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestCourtDTO {
    private String name;
    private String address;
    private String ownerTeamName;
}
