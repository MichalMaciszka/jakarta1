package org.example.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    private String login;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ToString.Exclude
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ToString.Exclude
    private byte[] portrait;
}
