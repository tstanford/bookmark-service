package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.User;
import com.timstanford.bookmarkservice.data.UserRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminControllerImpl implements AdminController {
    private UserRepository userRepository;

    public AdminControllerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
