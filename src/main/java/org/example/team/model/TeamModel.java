package org.example.team.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.driver.dto.GetDriverResponse;
import org.example.team.entity.Team;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamModel implements Serializable {
    private String nationality;

    private Integer championshipsWon;

    private String teamChief;

    private Boolean isActive;

    public static Function<Team, TeamModel> entityToModelMapper() {
        return team -> TeamModel.builder()
                .championshipsWon(team.getChampionshipsWon())
                .teamChief(team.getTeamChief())
                .isActive(team.getIsActive())
                .nationality(team.getNationality())
                .build();
    }
}
