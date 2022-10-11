package org.example.driver.dto;

import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.driver.entity.Driver;

@Data
@AllArgsConstructor
@Builder
public class GetDriverResponse {
    private Integer number;
    private String name;
    private String surname;
    private String nationality;
    private Integer racesWon;
    private String team;

    public static Function<Driver, GetDriverResponse> entityToDtoMapper() {
        return entity -> GetDriverResponse.builder()
                .number(entity.getStartingNumber())
                .name(entity.getName())
                .surname(entity.getSurname())
                .nationality(entity.getNationality())
                .racesWon(entity.getRacesWon())
                .team(entity.getTeam().getTeamName())
                .build();
    }
}
