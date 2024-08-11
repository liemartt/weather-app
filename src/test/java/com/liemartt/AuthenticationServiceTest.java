package com.liemartt;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.User;
import com.liemartt.service.AuthenticationService;
import com.liemartt.service.LoginService;
import com.liemartt.service.SignupService;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class AuthenticationServiceTest {
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    private final SignupService signupService = SignupService.getINSTANCE();
    private final LoginService loginService = LoginService.getINSTANCE();
    
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
        signupService.signupNewUser(new UserDto(user.getUsername(), user.getPassword()));
        com.liemartt.entity.Session session = loginService.loginUser(userDto);
        String sessionId = session.getId().toString();
        
        Assertions.assertTrue(authenticationService.isSessionValid(sessionId));
        Assertions.assertEquals(user, authenticationService.getAuthorizedUser(sessionId).get());
    }
    
    @Test
    void authenticate_userWithExpiredSession_failureAuth() {
        User user = new User("UserWithExpiredSession", "password");
        UserDto userDto = new UserDto(user);
        signupService.signupNewUser(new UserDto(user.getUsername(), user.getPassword()));
        com.liemartt.entity.Session session = loginService.loginUser(userDto);
        String sessionId = session.getId().toString();
        
        sessionDAO.endSession(session.getId());
        
        Assertions.assertFalse(authenticationService.isSessionValid(sessionId));
    }
    
    @Test
    void authenticate_userWithWrongSessionId_failureAuth() {
        Assertions.assertFalse(authenticationService.isSessionValid("Wrong session id"));
        Assertions.assertEquals(Optional.empty(), authenticationService.getAuthorizedUser("Wrong session id"));
    }
    
    
}
