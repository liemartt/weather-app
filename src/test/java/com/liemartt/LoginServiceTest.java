package com.liemartt;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;
import com.liemartt.dao.UserDAO;
import com.liemartt.dao.UserDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.User;
import com.liemartt.exception.IncorrectPasswordException;
import com.liemartt.exception.UserNotFoundException;
import com.liemartt.service.LoginService;
import com.liemartt.service.SignupService;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LoginServiceTest {
    private final LoginService loginService = LoginService.getINSTANCE();
    private final SignupService signupService = SignupService.getINSTANCE();
    private final UserDAO userDAO = new UserDAOImpl();
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    
    @BeforeAll
    static void configureHibernate() {
        TestConfig.configure();
    }
    
    
    @AfterAll
    static void clearDb() {
        Session session = TestConfig.getSession();
        session.beginTransaction();
        session.createMutationQuery("DELETE FROM User").executeUpdate();
        session.createMutationQuery("DELETE FROM Session").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
    
    @Test
    void login_existingUser_successfulLogin() {
        User user = new User("ExistingUser", "password");
        UserDto userDto = new UserDto(user);
        signupService.signupNewUser(new UserDto(user.getUsername(), user.getPassword()));
        
        com.liemartt.entity.Session session = loginService.loginUser(userDto);
        
        Assertions.assertNotNull(sessionDAO.getSessionByUUID(session.getId()).map(com.liemartt.entity.Session::getId));
    }
    
    @Test
    void login_nonExistingUser_failureLogin() {
        UserDto userDto = new UserDto("NotExistingUser", "password");
        
        Assertions.assertThrows(UserNotFoundException.class, () -> loginService.loginUser(userDto));
    }
    
    @Test
    void login_incorrectPassword_failureLogin() {
        User user = new User("UserWithIncorrectPassword", "password");
        signupService.signupNewUser(new UserDto(user));
        UserDto userDto = new UserDto("UserWithIncorrectPassword", "fakePassword");
        
        Assertions.assertThrows(IncorrectPasswordException.class, () -> loginService.loginUser(userDto));
    }
}
