package com.timstanford.bookmarkservice.security;

import com.timstanford.bookmarkservice.api.UserNotFoundException;
import com.timstanford.bookmarkservice.data.User;
import com.timstanford.bookmarkservice.data.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    public int registerUser(String username, String email, String password) {
        var user = repository.findByUsernameIgnoreCase(username);
        if (user.isPresent()){
            throw new UsernameAlreadyExistsException(username);
        }
        User newUser = new User(username.toLowerCase(), email, passwordEncoder.encode(password));
        return repository.save(newUser).getUserId();
    }

    public void updateUser(int id, String email, String password) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setEmailAddress(email);
        repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.isAdmin()) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(), authorityList);
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }
}
