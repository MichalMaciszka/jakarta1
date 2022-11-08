package org.example.team.view.converter;

import org.example.team.entity.Team;
import org.example.team.model.TeamModel;
import org.example.team.service.TeamService;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.Optional;

@FacesConverter(forClass = TeamModel.class, managed = true)
public class TeamConverter implements Converter<TeamModel> {

    private final TeamService service;

    @Inject
    public TeamConverter(TeamService service) {
        this.service = service;
    }

    @Override
    public TeamModel getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        Optional<Team> team = service.findTeam(s);
        return team.isEmpty() ? null : TeamModel.entityToModelMapper().apply(team.get());
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, TeamModel teamModel) {
        return teamModel == null ? "" : teamModel.getName();
    }
}
