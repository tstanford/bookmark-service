package com.timstanford.bookmarkservice.api;

public record NewUserRequest(String username, String emailAddress, String password) {}
