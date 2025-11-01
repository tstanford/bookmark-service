package com.timstanford.bookmarkservice.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RestController
public class AdminCheckController {
    @GetMapping("/amiadmin")
    public Boolean amiAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .map(a -> (GrantedAuthority) a)
                .anyMatch(ga -> ga.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }
}
