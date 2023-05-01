package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void createUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        return entityManager.createQuery(" FROM User").getResultList();
    }

    @Override
    public User getUser(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void updateUser(Long id, User user) {
        User edit = entityManager.find(User.class, id);
        edit.setUserName(user.getUserName());
        edit.setPassword(user.getPassword());
    }

    @Override
    public void removeUser(Long id) {
        entityManager.remove(entityManager.find(User.class, id));
        entityManager.flush();
    }

    @Override
    public User findByLogin(String userName) {
        List<User> users = entityManager.createQuery(
                        "select u from User u join fetch u.roles where u.userName=:userName", User.class)
                .setParameter("userName", userName)
                .getResultList();

        return !users.isEmpty() ? users.get(0) : null;
    }
}
