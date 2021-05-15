package com.example.testing.privatemethod.service;

import org.springframework.stereotype.Service;

/**
 * @author star
 * @date 2021/5/15
 */
@Service
public class UserService2 {

    public String getName(String id) {
        return this.getNameById(id);
    }

    private String getNameById(String id) {
        return "test-" + id;
    }

}
