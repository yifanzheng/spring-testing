package com.example.testing.mockito;

import com.example.testing.mockito.repository.User;
import com.example.testing.mockito.repository.UserRepository;
import com.example.testing.mockito.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 使用 Mockito.mock()、@Mock 进行 mock
 *
 * @author star
 */
// @RunWith(MockitoJUnitRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    private User mockUser() {
        User user = new User();
        user.setName("mock-test-name");
        user.setEmail("mock-test-email");

        return user;
    }

    @Before
    public void setup() {
        // 当使用 @Mock 注解时，
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUser_MockMethod() {
        // 模拟 UserRepository，测试时不直接操作数据库
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        // 将 mockUserRepository 注入到 UserService 类中
        userService.setUserRepository(mockUserRepository);

        User mockUser = mockUser();
        Mockito.when(mockUserRepository.getUser(mockUser.getName()))
                .thenReturn(mockUser);

        User user = userService.getUser(mockUser.getName());
        Assert.assertEquals(mockUser.getName(), user.getName());
        Assert.assertEquals(mockUser.getEmail(), user.getEmail());

        // 验证 mockUserRepository.getUser() 方法是否执行
        Mockito.verify(mockUserRepository).getUser(mockUser.getName());
    }

    @Test
    public void testGetUser_MockAnnotation() {
        User mockUser = mockUser();
        Mockito.when(userRepository.getUser(mockUser.getName()))
                .thenReturn(mockUser);

        User user = userService.getUser(mockUser.getName());
        Assert.assertEquals(mockUser.getName(), user.getName());
        Assert.assertEquals(mockUser.getEmail(), user.getEmail());

        // 验证 mockUserRepository.getUser() 方法是否执行
        Mockito.verify(userRepository).getUser(mockUser.getName());
    }

}
