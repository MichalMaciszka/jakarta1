package org.example.team.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.example.team.dto.GetTeamResponse;
import org.example.team.entity.Team;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamsModel implements Serializable {
    @Singular
    private List<GetTeamResponse> teams;

    public static Function<Collection<Team>, TeamsModel> entityToModelMapper() {
        return t -> {
            TeamsModelBuilder builder = TeamsModel.builder();
            List<GetTeamResponse> responses = t.stream()
                    .map(x -> GetTeamResponse.entityToDtoMapper().apply(x))
                    .collect(Collectors.toList());
            return builder.teams(responses).build();
        };
    }
}
