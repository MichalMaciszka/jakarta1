package org.example.user.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.NoArgsConstructor;
import org.example.user.entity.User;
import org.example.user.repository.UserRepository;
import org.example.utils.PortraitUtils;

@ApplicationScoped
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        userRepository.createUser(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findUser(login);
    }

    public void updatePortrait(String filePath,  String login, byte[] portrait) throws IOException {
        PortraitUtils.saveImage(filePath, login, portrait);
        userRepository.updatePortrait(login, portrait);
    }

    public void deletePortrait(String filePath, String login) throws IOException {
        userRepository.updatePortrait(login, null);
        PortraitUtils.delete(filePath, login);
    }
}
