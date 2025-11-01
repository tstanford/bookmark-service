package com.timstanford.bookmarkservice.api.exceptions;

import com.timstanford.bookmarkservice.api.AdminController;
import com.timstanford.bookmarkservice.data.User;
import com.timstanford.bookmarkservice.data.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public Boolean amiAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(a -> (GrantedAuthority) a)
                .anyMatch(ga -> ga.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));

        return isAdmin;
    }
}
