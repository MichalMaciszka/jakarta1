package org.example.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateTeamRequest {
    private String nationality;
    private Integer championshipsWon;
    private String teamChief;
    private Boolean isActive;
}
