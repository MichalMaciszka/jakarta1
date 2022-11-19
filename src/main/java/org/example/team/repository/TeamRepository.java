package org.example.team.repository;

import lombok.NoArgsConstructor;
import org.example.team.entity.Team;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Dependent
@NoArgsConstructor
public class TeamRepository {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void createTeam(Team team) {
        em.persist(team);
    }

    public List<Team> findAllTeams() {
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }

    public Optional<Team> findTeam(String name) {
        return Optional.ofNullable(em.find(Team.class, name));
    }

    public void deleteTeam(Team team) {
        em.remove(em.find(Team.class, team.getTeamName()));
    }

    public void deleteAll() {
        findAllTeams().forEach(em::remove);
    }

    public void updateTeam(Team team) {
        em.merge(team);
    }
}
