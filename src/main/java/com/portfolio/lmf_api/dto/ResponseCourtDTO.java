package com.portfolio.lmf_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseCourtDTO extends RequestCourtDTO {
    private List<MatchDTO> matches;

    public ResponseCourtDTO(String name, String address, String ownerTeamName, List<MatchDTO> matches) {
        super(name, address, ownerTeamName);
        this.matches = matches;
    }
}
