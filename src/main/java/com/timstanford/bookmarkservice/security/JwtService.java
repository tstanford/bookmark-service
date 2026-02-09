package com.timstanford.bookmarkservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final long MILLSECONDS_IN_A_DAY = 86400000;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.refresh-token-expiration-days:90}")
    private long refreshTokenExpirationDays;

    @Value("${jwt.auth-token-expiration-days:14}")
    private long authTokenExpirationDays;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Authentication authentication) {
        return generateToken(authentication, authTokenExpirationDays * MILLSECONDS_IN_A_DAY)
                .setHeaderParam("TokenType", "Auth")
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshTokenExpirationDays * MILLSECONDS_IN_A_DAY)
                .setHeaderParam("TokenType", "Refresh")
                .compact();
    }

    private JwtBuilder generateToken(Authentication authentication, long expiration) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean isRefreshToken(String token) {
        var type = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parse(token)
                .getHeader()
                .get("TokenType")
                .toString();
        return type.equals("Refresh");
    }

    public boolean isAuthToken(String token) {
        var type = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parse(token)
                .getHeader()
                .get("TokenType")
                .toString();
        return type.equals("Auth");
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
