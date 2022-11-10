package org.example.user.service;

import lombok.NoArgsConstructor;
import org.example.user.entity.User;
import org.example.user.repository.UserRepository;
import org.example.utils.PortraitUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void create(User user) throws RollbackException {
        userRepository.createUser(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findUser(login);
    }

    @Transactional
    public void updatePortrait(String filePath, String login, byte[] portrait) throws IOException {
        PortraitUtils.saveImage(filePath, login, portrait);
        userRepository.updatePortrait(login, portrait);
    }

    @Transactional
    public void deletePortrait(String filePath, String login) throws IOException {
        userRepository.updatePortrait(login, null);
        PortraitUtils.delete(filePath, login);
    }
}
