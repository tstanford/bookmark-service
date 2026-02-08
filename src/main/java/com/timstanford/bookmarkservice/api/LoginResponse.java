package com.timstanford.bookmarkservice.api;

public record LoginResponse(String authToken, String refreshToken) {
}
