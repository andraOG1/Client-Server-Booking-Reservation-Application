package org.example.dto;

import java.io.Serializable;


public class UserDTO implements Serializable{
    private String username;
    private String password;


    public UserDTO(String username, String passwd) {
        this.username = username;
        this.password = passwd;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
