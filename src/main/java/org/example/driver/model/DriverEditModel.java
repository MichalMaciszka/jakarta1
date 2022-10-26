package org.example.driver.model;


import lombok.*;
import org.example.driver.entity.Driver;
import org.example.team.entity.Team;
import org.example.team.model.TeamModel;

import java.util.function.BiFunction;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverEditModel {
    private String name;
    private String surname;
    private String nationality;
    private Integer racesWon;
    private TeamModel teamModel;

    public static Function<Driver, DriverEditModel> entityToModelMapper() {
        return d -> DriverEditModel.builder()
                .name(d.getName())
                .surname(d.getSurname())
                .nationality(d.getNationality())
                .racesWon(d.getRacesWon())
                .teamModel(TeamModel.entityToModelMapper().apply(d.getTeam()))
                .build();
    }

    public static BiFunction<Driver, DriverEditModel, Driver> updater(
            Function<TeamModel, Team> teamMapper
    ) {
        return (driver, request) -> {
            driver.setName(request.getName());
            driver.setSurname(request.getSurname());
            driver.setNationality(request.getNationality());
            driver.setRacesWon(request.getRacesWon());
            driver.setTeam(teamMapper.apply(request.getTeamModel()));
            return driver;
        };
    }
}
