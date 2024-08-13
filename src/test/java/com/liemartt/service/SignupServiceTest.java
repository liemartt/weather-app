package com.liemartt.service;

import com.liemartt.TestConfig;
import com.liemartt.dao.UserDAO;
import com.liemartt.dao.UserDAOImpl;
import com.liemartt.dto.UserDto;
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
        session.createMutationQuery("DELETE FROM User")
                .executeUpdate();
        session.getTransaction()
                .commit();
        session.close();
    }
    
    @Test
    void signup_uniqueUsername_successfulSignup() {
        UserDto userDto = new UserDto("unique user", "password");
        
        signupService.signupNewUser(userDto);
        
        Assertions.assertNotNull(userDAO.getUserByUsername(userDto.getUsername())
                .get());
    }
    
    @Test
    void signup_nonUniqueUsername_failureSignup() {
        UserDto userDto = new UserDto("non unique user", "password");
        
        signupService.signupNewUser(userDto);
        
        Assertions.assertThrows(
                UsernameAlreadyExistsException.class,
                () -> signupService.signupNewUser(userDto));
    }
}
