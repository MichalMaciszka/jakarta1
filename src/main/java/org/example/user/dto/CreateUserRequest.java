package org.example.user.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.user.entity.User;
import org.example.user.entity.UserRole;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String login;
    private String userRole;
    private String password;
    private String birthDate;

    public static Function<CreateUserRequest, User> dtoToEntityMapper() {
        return request -> User
                .builder()
                .login(request.getLogin())
                .password(request.getPassword())
                .role(UserRole.valueOf(request.getUserRole()))
                .birthDate(LocalDate.parse(request.getBirthDate(), DateTimeFormatter.ISO_LOCAL_DATE))
                .build();
    }
}
