package com.liemartt.service;

import com.liemartt.dao.UserDAO;
import com.liemartt.dao.UserDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.User;
import com.liemartt.exception.UsernameExistsException;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

public class SignupService {
    @Getter
    private final static SignupService INSTANCE = new SignupService();
    private final UserDAO userDAO = new UserDAOImpl();
    
    private SignupService() {
    }
    
    public void signupNewUser(UserDto userDto) throws UsernameExistsException {
        String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
        User user = new User(userDto.getUsername(), hashedPassword);
        userDAO.save(user);
    }
}
