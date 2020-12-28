package com.example.testing.resttemplate.service;

import com.example.testing.resttemplate.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * UserService
 *
 * @author star
 */
@Service("userServiceRest")
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    public List<UserVO> getUsers() {
        UserVO[] users = restTemplate.getForObject("http://localhost:8080/users", UserVO[].class);
        if (users == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(users);
    }
}
