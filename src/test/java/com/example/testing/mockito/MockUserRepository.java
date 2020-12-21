package com.example.testing.mockito;

import com.example.testing.mockito.repository.User;
import com.example.testing.mockito.repository.UserRepository;
import org.springframework.stereotype.Repository;

/**
 * MockUserRepository
 *
 * @author star
 */
public class MockUserRepository extends UserRepository {

    /**
     * 模拟从数据库中获取用户信息
     */
    @Override
    public User getUser(String name) {
        User user = new User();
        user.setName("mock-test-name");
        user.setEmail("mock-test-email");

        return user;
    }
}
