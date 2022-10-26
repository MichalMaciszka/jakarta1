package org.example.driver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;
import org.example.team.entity.Team;
import org.example.team.model.TeamModel;

import java.util.function.Function;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverCreateModel {
    private Integer startingNumber;
    private String name;
    private String surname;
    private String nationality;
    private Integer racesWon;
    private TeamModel teamModel;

    public static Function<Driver, DriverCreateModel> entityToModelMapper() {
        return d -> DriverCreateModel.builder()
                .startingNumber(d.getStartingNumber())
                .name(d.getName())
                .surname(d.getSurname())
                .nationality(d.getNationality())
                .racesWon(d.getRacesWon())
                .teamModel(TeamModel.entityToModelMapper().apply(d.getTeam()))
                .build();
    }

    public static Function<DriverCreateModel, Driver> modelToEntityMapper(
            Function<TeamModel, Team> teamMapper
    ) {
        return model -> Driver.builder()
                .name(model.getName())
                .surname(model.getSurname())
                .nationality(model.getNationality())
                .racesWon(model.getRacesWon())
                .startingNumber(model.getStartingNumber())
                .team(teamMapper.apply(model.getTeamModel()))
                .build();
    }
}
