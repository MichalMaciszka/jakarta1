package org.example.user.entity;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String login;

    private UserRole role;

    @ToString.Exclude
    private String password;

    private LocalDate birthDate;

    @ToString.Exclude
    private byte[] portrait;
}
