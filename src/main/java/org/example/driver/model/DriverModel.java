package org.example.driver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;

import java.io.Serializable;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverModel implements Serializable {
    private Integer startingNumber;
    private String name;
    private String surname;
    private String nationality;
    private Integer racesWon;
    private String teamName;

    public static Function<Driver, DriverModel> entityToModelMapper() {
        return d -> DriverModel.builder()
                .startingNumber(d.getStartingNumber())
                .name(d.getName())
                .surname(d.getSurname())
                .nationality(d.getNationality())
                .racesWon(d.getRacesWon())
                .teamName(d.getTeam().getTeamName())
                .build();
    }
}
