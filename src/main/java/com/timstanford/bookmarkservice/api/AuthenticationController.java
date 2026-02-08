package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.security.JwtService;
import com.timstanford.bookmarkservice.security.LoginRequest;
import com.timstanford.bookmarkservice.security.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtService jwtService,
                                    UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String authToken = jwtService.generateToken(authentication);
        String refreshToken = jwtService.generateRefreshToken(authentication);

        var response = new LoginResponse(authToken, refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshAuthToken(@RequestBody String refreshToken) {
        var username = jwtService.extractUsername(refreshToken);
        var user = userService.loadUserByUsername(username);

        if(jwtService.isRefreshToken(refreshToken) &&
                user.isAccountNonLocked() &&
                !jwtService.isTokenExpired(refreshToken)){
            Authentication auth = new UsernamePasswordAuthenticationToken(username, "");
            String newAuthToken = jwtService.generateToken(auth);
            String newRefreshToken = jwtService.generateRefreshToken(auth);
            var response = new LoginResponse(newAuthToken, newRefreshToken);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
