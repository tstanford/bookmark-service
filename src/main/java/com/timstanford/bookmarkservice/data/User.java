package com.timstanford.bookmarkservice.data;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "[user]")
public class User {
    @GeneratedValue
    @Id
    private int userId;

    @Column(unique=true)
    private String username;

    private String password;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "is_admin")
    @ColumnDefault("false")
    private boolean isAdmin;

    public User() {
    }

    public User(String username, String emailAddress, String password) {
        this();
        this.username = username;
        this.emailAddress = emailAddress;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
