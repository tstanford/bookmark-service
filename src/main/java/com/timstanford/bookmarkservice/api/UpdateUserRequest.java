package com.timstanford.bookmarkservice.api;

public record UpdateUserRequest(String emailAddress, String password) {
}
