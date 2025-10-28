package com.timstanford.bookmarkservice.data;

import jakarta.persistence.*;

@Entity
@Table(name = "[user]")
public class User {
    @GeneratedValue
    @Id
    private int userId;

    @Column(unique=true)
    private String username;

    private String password;

    public User() {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
