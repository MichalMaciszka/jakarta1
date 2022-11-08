package org.example.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.team.entity.Team;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTeamsResponse {
    private List<GetTeamResponse> teams;

    public static Function<Collection<Team>, GetTeamsResponse> entityToDtoMapper() {
        return x -> new GetTeamsResponse(
                x.stream()
                        .map(team -> GetTeamResponse.entityToDtoMapper().apply(team))
                        .collect(Collectors.toList())
        );
    }
}
