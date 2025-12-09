package com.timstanford.bookmarkservice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public interface AdminController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    List<UserWithStats> getUsers();

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user")
    ResponseEntity<Integer> registerNewUser(@RequestBody NewUserRequest newUserRequest);

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/{id}")
    ResponseEntity<Void> updateUser(@PathVariable int id, @RequestBody UpdateUserRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}")
    ResponseEntity<Void> deleteUser(int id);
}
