package com.timstanford.bookmarkservice.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public interface AdminController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    Object getUsers();
}
