package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public interface AdminController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    List<User> getUsers();
}
