package com.example.testing.mockito;

import com.example.testing.mockito.repository.User;
import com.example.testing.mockito.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 使用 @MockBean 进行 mock
 *
 * @author star
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceMockBeanTest {

    @MockBean
    private UserRepository userRepository;

    private User mockUser() {
        User user = new User();
        user.setName("mock-test-name");
        user.setEmail("mock-test-email");

        return user;
    }

    @Test
    public void testGetUser_MockBean() {
        User mockUser = mockUser();
        // 模拟 UserRepository
        Mockito.when(userRepository.getUser(mockUser.getName()))
                .thenReturn(mockUser);
        // 验证结果
        User user = userRepository.getUser(mockUser.getName());
        Assert.assertEquals(mockUser.getName(), user.getName());
        Assert.assertEquals(mockUser.getEmail(), user.getEmail());

        Mockito.verify(userRepository).getUser(mockUser.getName());
    }
}
