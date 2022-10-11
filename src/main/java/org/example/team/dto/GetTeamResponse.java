package org.example.team.dto;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.team.entity.Team;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GetTeamResponse {
    private String teamName;
    private String nationality;
    private String championshipsWon;
    private String teamChief;

    public static Function<Team, GetTeamResponse> entityToDtoMapper() {
        return team -> GetTeamResponse.builder()
                .teamName(team.getTeamName())
                .nationality(team.getNationality())
                .championshipsWon(team.getChampionshipsWon().toString())
                .teamChief(team.getTeamChief())
                .build();
    }
}
