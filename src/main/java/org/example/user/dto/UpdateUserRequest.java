package org.example.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.user.entity.User;

import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class UpdateUserRequest {
    private String password;
    private List<String> userRoles;
    private String birthDate;

    public static Function<UpdateUserRequest, User> dtoToEntityMapper(Pbkdf2PasswordHash hash, String login) {
        return request -> User.builder()
                .roles(request.getUserRoles())
                .birthDate(LocalDate.parse(request.getBirthDate(), DateTimeFormatter.ISO_LOCAL_DATE))
                .login(login)
                .password(hash.generate(request.getPassword().toCharArray()))
                .portrait(null)
                .build();
    }
}
