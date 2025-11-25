package com.timstanford.bookmarkservice.api;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("User not found by id "+id);
    }
}
