package com.example.testing.privatemethod;

import com.example.testing.TestApplication;
import com.example.testing.privatemethod.service.UserService2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * UserService2Test
 *
 * @author star
 * @date 2021/5/15
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UserService2.class})
@SpringBootTest(classes = TestApplication.class)
public class UserService2Test {

    @InjectMocks
    private UserService2 userService;

    /**
     * 模拟测试方法
     */
    @Test
    public void testGetName_mockPrivateMethod() throws Exception {
        // mock private method
        String userId = "xxx";
        UserService2 mockUserService = PowerMockito.spy(userService);
        PowerMockito.when(mockUserService, "getNameById", userId)
                .thenReturn("test-" + userId);
        String name = mockUserService.getName(userId);

        Assert.assertEquals("test-" + userId, name);

    }

    /**
     * 测试私有方法一
     */
    @Test
    public void testGetNameById() throws InvocationTargetException, IllegalAccessException {
        // test private method
        String userId = "xxx";
        Method getNameById = PowerMockito.method(UserService2.class, "getNameById", String.class);
        Object result = getNameById.invoke(userService, userId);

        Assert.assertEquals("test-" + userId, result);
    }

    /**
     * 测试私有方法二
     */
    @Test
    public void testGetNameById_2() throws Exception {
        // test private method
        String userId = "xxx";
        Object result = Whitebox.invokeMethod(userService, "getNameById", userId);
        Assert.assertEquals("test-" + userId, result);
    }

}
