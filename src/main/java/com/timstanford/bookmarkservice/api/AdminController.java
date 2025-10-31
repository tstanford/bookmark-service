package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
public interface AdminController {
    @GetMapping("/users")
    List<User> getUsers();

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/amiadmin")
    Boolean amiAdmin();
}
