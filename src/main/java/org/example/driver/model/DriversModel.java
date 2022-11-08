package org.example.driver.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.example.driver.dto.GetDriverResponse;
import org.example.driver.entity.Driver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriversModel implements Serializable {
    @Singular
    private List<GetDriverResponse> drivers;

    public static Function<Collection<Driver>, DriversModel> entityToModelMapper() {
        return x -> {
            var responses = x.stream()
                    .map(a -> GetDriverResponse.entityToDtoMapper().apply(a))
                    .collect(Collectors.toList());
            return DriversModel.builder()
                    .drivers(responses)
                    .build();
        };
    }
}
