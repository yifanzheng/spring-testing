package com.example.testing.resttemplate.web;

import com.example.testing.resttemplate.service.UserService;
import com.example.testing.resttemplate.vo.UserVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author star
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("userServiceRest")
    @InjectMocks
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUsers() {
        UserVO mockUser = new UserVO(1, "mock-test");

        Mockito.when(restTemplate.getForObject("http://localhost:8080/users", UserVO[].class))
                .thenReturn(new UserVO[]{mockUser});
        List<UserVO> users = userService.getUsers();
        Assert.assertFalse(CollectionUtils.isEmpty(users));
        Assert.assertEquals(1, users.size());
        UserVO userVO = users.get(0);
        Assert.assertEquals(mockUser.getId(), userVO.getId());
        Assert.assertEquals(mockUser.getName(), userVO.getName());
    }
}
