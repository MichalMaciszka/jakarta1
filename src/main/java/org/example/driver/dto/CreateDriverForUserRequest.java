package org.example.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateDriverForUserRequest {
    private Integer startingNumber;
    private String name;
    private String surname;
    private String nationality;
    private Integer racesWon;
    private String teamName;
}
