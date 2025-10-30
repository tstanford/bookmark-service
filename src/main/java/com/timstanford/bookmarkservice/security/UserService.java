package com.timstanford.bookmarkservice.security;

import com.timstanford.bookmarkservice.data.User;
import com.timstanford.bookmarkservice.data.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;

        this.repository = repository;
    }

    public int registerUser(String username, String password) {
        User newUser = new User(username.toLowerCase(), passwordEncoder.encode(password));
        return repository.save(newUser).getUserId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),new ArrayList<>());
    }
}
