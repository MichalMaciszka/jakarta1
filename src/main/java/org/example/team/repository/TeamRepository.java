package org.example.team.repository;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.example.datastore.DataStore;
import org.example.team.entity.Team;

@Dependent
public class TeamRepository {
    private final DataStore dataStore;

    @Inject
    public TeamRepository(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public void createTeam(Team team) {
        dataStore.createTeam(team);
    }

    public List<Team> findAllTeams() {
        return dataStore.findAllTeams();
    }

    public Optional<Team> findTeam(String name) {
        return dataStore.findTeamByName(name);
    }

    public void deleteTeam(String teamName) {
        dataStore.deleteTeam(teamName);
    }
}
