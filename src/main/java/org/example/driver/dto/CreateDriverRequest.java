package org.example.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateDriverRequest {
    private String startingNumber;
    private String name;
    private String surname;
    private String nationality;
    private String racesWon;
    private String teamName;
    private String userLogin;
}
