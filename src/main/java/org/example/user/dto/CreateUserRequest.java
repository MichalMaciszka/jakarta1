package org.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.user.entity.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String login;
    private List<String> userRoles;
    private String password;
    private String birthDate;

    public static Function<CreateUserRequest, User> dtoToEntityMapper() {
        return request -> User
                .builder()
                .login(request.getLogin())
                .password(request.getPassword())
                .roles(request.getUserRoles())
                .birthDate(LocalDate.parse(request.getBirthDate(), DateTimeFormatter.ISO_LOCAL_DATE))
                .build();
    }
}
