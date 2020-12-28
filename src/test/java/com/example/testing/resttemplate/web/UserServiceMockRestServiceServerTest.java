package com.example.testing.resttemplate.web;

import com.example.testing.resttemplate.service.UserService;
import com.example.testing.resttemplate.vo.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * @author star
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceMockRestServiceServerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetUsers() throws Exception {
        UserVO mockUser = new UserVO(1, "mock-test");
        // 模拟 RestTemplate 请求
        mockServer.expect(ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(new URI("http://localhost:8080/users")))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new UserVO[]{mockUser}))
                );
        List<UserVO> users = userService.getUsers();
        Assert.assertFalse(CollectionUtils.isEmpty(users));
        UserVO userVO = users.get(0);
        Assert.assertEquals(mockUser.getId(), userVO.getId());
        Assert.assertEquals(mockUser.getName(), userVO.getName());
        mockServer.verify();
    }

}