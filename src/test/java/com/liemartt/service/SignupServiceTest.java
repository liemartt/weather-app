package com.liemartt.service;

import com.liemartt.TestConfig;
import com.liemartt.dao.UserDAO;
import com.liemartt.dao.UserDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.User;
import com.liemartt.exception.UsernameAlreadyExistsException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class SignupServiceTest {
    private final SignupService signupService = SignupService.getINSTANCE();
    private final UserDAO userDAO = new UserDAOImpl();
    
    
    @BeforeAll
    static void configureHibernate() {
        TestConfig.configure();
    }
    
    
    @AfterAll
    static void clearDb() {
        Session session = TestConfig.getSession();
        session.beginTransaction();
        session.createMutationQuery("DELETE FROM User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    
    @Test
    void signup_uniqueUsername_successfulSignup() {
        User user = new User("User", "password");
        
        signupService.signupNewUser(new UserDto(user.getUsername(), user.getPassword()));
        
        Assertions.assertNotNull(userDAO.getUserByUsername("Artem").get());
    }
    
    @Test
        void signup_nonUniqueUsername_failureSignup() {
        User user = new User("User", "password2");
        
        Assertions.assertThrows(
                UsernameAlreadyExistsException.class,
                () -> signupService.signupNewUser(new UserDto(user.getUsername(), user.getPassword())));
    }
}
