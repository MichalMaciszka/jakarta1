package org.example.user.service;

import lombok.NoArgsConstructor;
import org.example.user.entity.User;
import org.example.user.repository.UserRepository;
import org.example.utils.PortraitUtils;

import javax.annotation.security.PermitAll;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Stateless
@LocalBean
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;

    private SecurityContext securityContext;

    private Pbkdf2PasswordHash hash;

    @Inject
    public UserService(UserRepository userRepository, SecurityContext securityContext, Pbkdf2PasswordHash hash) {
        this.userRepository = userRepository;
        this.securityContext = securityContext;
        this.hash = hash;
    }

    @PermitAll
    public void create(User user) throws EJBTransactionRolledbackException {
        userRepository.createUser(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findUser(login);
    }

    public void updatePortrait(String filePath, String login, byte[] portrait) throws IOException {
        PortraitUtils.saveImage(filePath, login, portrait);
        userRepository.updatePortrait(login, portrait);
    }

    public void deleteUser(User user) {
        userRepository.deleteUser(user);
    }

    public void deletePortrait(String filePath, String login) throws IOException {
        userRepository.updatePortrait(login, null);
        PortraitUtils.delete(filePath, login);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
}
