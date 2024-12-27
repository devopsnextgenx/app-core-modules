package io.devopsnextgenx.microservices.modules.security.repositories;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Repository;

import io.devopsnextgenx.microservices.modules.security.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
@EntityScan(basePackages = "io.devopsnextgenx.microservices.modules.security.models")
public class AppxUserRepositoryImpl implements AppxUserRepositoryCustom<User, String> {
    
    @PersistenceContext
    private EntityManager entityManager;

    public User findByUsername(String userName) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM USER u WHERE u.userName = :userName", User.class);
        query.setParameter("userName", userName);
        return query.getSingleResult();
    }

    
    public boolean existUser(String userName) {
        boolean exist = true;
        try {
            findByUsername(userName);
        } catch (Exception exception) {
            exist = false;
        }
        return exist;
    }

    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }
}
