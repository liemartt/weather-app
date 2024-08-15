package com.liemartt.service;

import com.liemartt.TestConfig;
import com.liemartt.dao.session.SessionDAO;
import com.liemartt.dao.session.SessionDAOImpl;
import com.liemartt.dao.user.UserDAO;
import com.liemartt.dao.user.UserDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.User;
import com.liemartt.exception.IncorrectPasswordException;
import com.liemartt.exception.UserNotFoundException;
import com.liemartt.exception.UsernameExistsException;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class AuthenticationServiceTest {
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    private final UserDAO userDAO = new UserDAOImpl();
    
    @BeforeAll
    static void configureHibernate() {
        TestConfig.configure();
    }
    
    
    @AfterAll
    static void clearDb() {
        Session session = TestConfig.getSession();
        session.beginTransaction();
        session.createMutationQuery("DELETE FROM Session").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    
    @Test
    void authenticate_userWithExistingSession_successfulAuth() {
        User user = new User("UserWithExistingSession", "password");
        UserDto userDto = new UserDto(user);
        authenticationService.signupNewUser(new UserDto(user.getUsername(), user.getPassword()));
        com.liemartt.entity.Session session = authenticationService.loginUser(userDto);
        String sessionId = session.getId().toString();
        
        Assertions.assertTrue(authenticationService.isSessionValid(sessionId));
        Assertions.assertEquals(user, authenticationService.getAuthorizedUser(sessionId).get());
    }
    
    @Test
    void authenticate_userWithExpiredSession_failureAuth() {
        User user = new User("UserWithExpiredSession", "password");
        UserDto userDto = new UserDto(user);
        authenticationService.signupNewUser(new UserDto(user.getUsername(), user.getPassword()));
        com.liemartt.entity.Session session = authenticationService.loginUser(userDto);
        String sessionId = session.getId().toString();
        
        sessionDAO.delete(session.getId());
        
        Assertions.assertFalse(authenticationService.isSessionValid(sessionId));
    }
    
    @Test
    void authenticate_userWithWrongSessionId_failureAuth() {
        Assertions.assertFalse(authenticationService.isSessionValid("Wrong session id"));
        Assertions.assertEquals(Optional.empty(), authenticationService.getAuthorizedUser("Wrong session id"));
    }
    
    @Test
    void login_existingUser_successfulLogin() {
        User user = new User("ExistingUser", "password");
        UserDto userDto = new UserDto(user);
        authenticationService.signupNewUser(new UserDto(user.getUsername(), user.getPassword()));
        
        com.liemartt.entity.Session session = authenticationService.loginUser(userDto);
        
        Assertions.assertNotNull(sessionDAO.findById(session.getId()).map(com.liemartt.entity.Session::getId));
    }
    
    @Test
    void login_nonExistingUser_failureLogin() {
        UserDto userDto = new UserDto("NotExistingUser", "password");
        
        Assertions.assertThrows(UserNotFoundException.class, () -> authenticationService.loginUser(userDto));
    }
    
    @Test
    void login_incorrectPassword_failureLogin() {
        User user = new User("UserWithIncorrectPassword", "password");
        authenticationService.signupNewUser(new UserDto(user));
        UserDto userDto = new UserDto("UserWithIncorrectPassword", "fakePassword");
        
        Assertions.assertThrows(IncorrectPasswordException.class, () -> authenticationService.loginUser(userDto));
    }
    
    @Test
    void signup_uniqueUsername_successfulSignup() {
        UserDto userDto = new UserDto("unique user", "password");
        
        authenticationService.signupNewUser(userDto);
        
        Assertions.assertNotNull(userDAO.findByUsername(userDto.getUsername())
                .get());
    }
    
    @Test
    void signup_nonUniqueUsername_failureSignup() {
        UserDto userDto = new UserDto("non unique user", "password");
        
        authenticationService.signupNewUser(userDto);
        
        Assertions.assertThrows(
                UsernameExistsException.class,
                () -> authenticationService.signupNewUser(userDto));
    }
    
    
}
