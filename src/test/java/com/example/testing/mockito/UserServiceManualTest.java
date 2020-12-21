package com.example.testing.mockito;

import com.example.testing.mockito.repository.User;
import com.example.testing.mockito.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author star
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceManualTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetUser_Manual() {
        // 将 MockUserRepository 注入到 UserService 中
        userService.setUserRepository(new MockUserRepository());
        User user = userService.getUser("mock-test-name");
        Assert.assertEquals("mock-test-name", user.getName());
        Assert.assertEquals("mock-test-email", user.getEmail());
    }
}
