package org.example.team.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teams")
public class Team implements Serializable {
    public Team(String teamName, String nationality, Integer championshipsWon, String teamChief, Boolean isActive) {
        this.teamName = teamName;
        this.nationality = nationality;
        this.championshipsWon = championshipsWon;
        this.teamChief = teamChief;
        this.isActive = isActive;
        this.drivers = new ArrayList<>();
    }

    @Id
    @Column(name = "team_name")
    private String teamName;

    private String nationality;

    @Column(name = "Championships_won")
    private Integer championshipsWon;

    @Column(name = "team_chief")
    private String teamChief;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Driver> drivers;
}
