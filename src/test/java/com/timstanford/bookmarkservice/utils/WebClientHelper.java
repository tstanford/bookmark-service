package com.timstanford.bookmarkservice.utils;

import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientHelper {
    private final String baseUrl;
    private String token;

    public WebClientHelper(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public @Nullable ResponseEntity<String> delete(String url) {
        WebClient webClient = WebClient.create(baseUrl + url);
        return webClient.delete()
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    public @Nullable ResponseEntity<String> getString(String url) {
        WebClient webClient = WebClient.create(baseUrl + url);
        ResponseEntity<String> result = webClient.get()
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .retrieve()
                .toEntity(String.class)
                .block();
        return result;
    }

    public @Nullable ResponseEntity<String> postString(String url, String payload) {
        WebClient webClient = WebClient.create(baseUrl + url);
        return webClient.post()
                .bodyValue(payload)
                .header("Content-Type", "application/json")
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    public @Nullable ResponseEntity<String> postStringWithAuth(String url, String payload) {
        WebClient webClient = WebClient.create(baseUrl + url);
        return webClient.post()
                .bodyValue(payload)
                .header("Content-Type", "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    public void setToken(String token) {
        this.token = token;
    }
}
