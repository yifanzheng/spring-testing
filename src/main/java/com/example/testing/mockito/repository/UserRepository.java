package com.example.testing.mockito.repository;

import org.springframework.stereotype.Repository;

/**
 * UserRepository
 *
 * @author star
 */
@Repository
public class UserRepository {

    /**
     * 模拟从数据库中获取用户信息，实际开发中需要连接真实的数据库
     */
    public User getUser(String name) {
        User user = new User();
        user.setName("testing");
        user.setEmail("testing@outlook.com");

        return user;
    }
}
