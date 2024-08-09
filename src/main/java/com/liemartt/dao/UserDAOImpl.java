package com.liemartt.dao;

import com.liemartt.entity.User;
import com.liemartt.exception.UserNotFoundException;
import com.liemartt.exception.UsernameAlreadyExistsException;
import com.liemartt.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    @Override
    public void saveUser(User user) throws UsernameAlreadyExistsException {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction()
                    .commit();
        } catch (ConstraintViolationException e) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
    }
    
    @Override
    public Optional<User> getUserByUsername(String username) throws UserNotFoundException {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            User user = session.createSelectionQuery("FROM User where username=:username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            session.getTransaction()
                    .commit();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
    }
    
    @Override
    public Optional<User> getUserById(Long id) throws UserNotFoundException {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            User user = session.createSelectionQuery("FROM User where id=:id", User.class)
                    .setParameter("username", id)
                    .getSingleResult();
            session.getTransaction()
                    .commit();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
    }
}
