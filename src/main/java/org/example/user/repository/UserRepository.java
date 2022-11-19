package org.example.user.repository;

import lombok.NoArgsConstructor;
import org.example.user.entity.User;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Dependent
@NoArgsConstructor
public class UserRepository {
    private EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void createUser(User user) {
        em.persist(user);
    }

    public List<User> findAllUsers() {
        return em.createQuery("select u from User u", User.class).getResultList();
    }

    public Optional<User> findUser(String login) {
        return Optional.ofNullable(em.find(User.class, login));
    }

    public void updatePortrait(String login, byte[] portrait) {
        User user = findUser(login).orElseThrow();
        em.detach(user);
        user.setPortrait(portrait);
        em.merge(user);
    }

    public void deleteUser(User user) {
        em.remove(em.find(User.class, user.getLogin()));
    }

    public void updateUser(User user) {
        em.merge(user);
    }
}
