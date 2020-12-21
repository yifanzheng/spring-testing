package com.example.testing.mockito.service;

import com.example.testing.mockito.repository.User;
import com.example.testing.mockito.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserService
 *
 * @author star
 */
@Service
public class UserService {

    private UserRepository userRepository;

    public User getUser(String name) {
        User user = userRepository.getUser(name);

        return user;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
