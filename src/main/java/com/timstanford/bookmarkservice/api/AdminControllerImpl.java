package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.UserRepository;
import com.timstanford.bookmarkservice.security.LoginRequest;
import com.timstanford.bookmarkservice.security.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminControllerImpl implements AdminController {
    private UserRepository userRepository;
    private UserService userService;

    public AdminControllerImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<UserWithStats> getUsers() {
        return userRepository.findAllUsersWithStats();
    }

    public ResponseEntity<Integer> registerNewUser(@RequestBody NewUserRequest newUserRequest) {
        int userId = userService.registerUser(newUserRequest.username(), newUserRequest.emailAddress(), newUserRequest.password());
        return ResponseEntity.ok(userId);
    }
}
