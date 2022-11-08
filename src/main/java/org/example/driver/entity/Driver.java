package org.example.driver.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.team.entity.Team;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver implements Serializable {
    private Integer startingNumber;
    private String name;
    private String surname;
    private String nationality;
    private Integer racesWon;

    private Team team;
}
