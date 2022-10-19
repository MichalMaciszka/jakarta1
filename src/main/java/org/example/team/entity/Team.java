package org.example.team.entity;

import java.io.Serializable;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team implements Serializable {
    private String teamName;
    private String nationality;
    private Integer championshipsWon;
    private String teamChief;
    private Boolean isActive;
}
