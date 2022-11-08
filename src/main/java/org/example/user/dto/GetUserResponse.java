package org.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.user.entity.User;

import java.time.LocalDate;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
public class GetUserResponse {
    private String login;
    private String userRole;
    private LocalDate birthDate;

    public static Function<User, GetUserResponse> entityToDtoMapper() {
        return user -> GetUserResponse
                .builder()
                .userRole(user.getRole().toString())
                .login(user.getLogin())
                .birthDate(user.getBirthDate())
                .build();
    }
}
