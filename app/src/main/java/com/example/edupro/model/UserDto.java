package com.example.edupro.model;

import java.io.Serializable;

public class UserDto implements Serializable {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email= email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
