package com.timstanford.bookmarkservice.data;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;

    public List<Role> getRoles() {
        return roles;
    }

    private List<Role> roles;

    public User() {
        roles = new ArrayList<>();
        roles.add(new Role("ROLE_USER"));
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
