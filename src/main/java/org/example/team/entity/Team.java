package org.example.team.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
