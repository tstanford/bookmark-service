package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetail implements UserDetailsService {

    private final List<User> users;
    private final PasswordEncoder passwordEncoder;

    public UserDetail(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.users = new ArrayList<>();
        users.add(new User("tim", passwordEncoder.encode("password123")));
        users.add(new User("bob", passwordEncoder.encode("blah")));
    }

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {
        var user = users.stream().filter(x -> x.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username"));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }
}
