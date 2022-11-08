package org.example.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.driver.entity.Driver;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetDriversResponse {
    private List<GetDriverResponse> drivers;

    public static Function<Collection<Driver>, GetDriversResponse> entityToDtoMapper() {
        return x -> new GetDriversResponse(
                x.stream()
                        .map(driver -> GetDriverResponse.entityToDtoMapper().apply(driver))
                        .collect(Collectors.toList())
        );
    }
}
