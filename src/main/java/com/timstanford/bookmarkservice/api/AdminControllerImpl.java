package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.UserRepository;
import com.timstanford.bookmarkservice.security.UserService;
import com.timstanford.bookmarkservice.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminControllerImpl implements AdminController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final BookmarkService bookmarkService;

    public AdminControllerImpl(UserRepository userRepository, UserService userService, BookmarkService bookmarkService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.bookmarkService = bookmarkService;
    }

    @Override
    public List<UserWithStats> getUsers() {
        return userRepository.findAllUsersWithStats();
    }

    public ResponseEntity<Integer> registerNewUser(@RequestBody NewUserRequest newUserRequest) {
        int userId = userService.registerUser(newUserRequest.username(), newUserRequest.emailAddress(), newUserRequest.password());
        return ResponseEntity.ok(userId);
    }

    @Override
    public ResponseEntity<Void> updateUser(int id, UpdateUserRequest request) {
        userService.updateUser(id, request.emailAddress(), request.password());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(int id) {
        bookmarkService.deleteAllForUser(id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
