package com.example.testing.resttemplate.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * UserVO
 *
 * @author star
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVO {

    private Integer id;

    private String name;

    public UserVO() {

    }

    public UserVO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
