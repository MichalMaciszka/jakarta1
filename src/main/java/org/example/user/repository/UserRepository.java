package org.example.user.repository;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.example.datastore.DataStore;
import org.example.user.entity.User;

@Dependent
public class UserRepository {
    private final DataStore store;

    @Inject
    public UserRepository(DataStore store) {
        this.store = store;
    }

    public void createUser(User user) {
        store.createUser(user);
    }

    public List<User> findAllUsers() {
        return store.findAllUsers();
    }

    public Optional<User> findUser(String login) {
        return store.findUserByLogin(login);
    }

    public void updatePortrait(String login, byte[] portrait) {
        store.updatePortrait(login, portrait);
    }
}
