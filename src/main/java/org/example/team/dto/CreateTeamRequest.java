package org.example.team.dto;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.team.entity.Team;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateTeamRequest {
    private String teamName;
    private String nationality;
    private String championshipsWon;
    private String teamChief;
    private Boolean isActive;

    public static Function<CreateTeamRequest, Team> dtoToEntityMapper() {
        return dto -> Team.builder()
                .teamName(dto.getTeamName())
                .nationality(dto.getNationality())
                .championshipsWon(Integer.parseInt(dto.getChampionshipsWon()))
                .teamChief(dto.getTeamChief())
                .isActive(dto.getIsActive())
                .build();
    }
}
