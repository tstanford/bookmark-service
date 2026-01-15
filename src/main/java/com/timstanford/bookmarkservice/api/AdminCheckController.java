package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.User;
import com.timstanford.bookmarkservice.data.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RestController
public class AdminCheckController {
    private final UserRepository userRepository;

    public AdminCheckController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/amiadmin")
    public Boolean amiAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .map(a -> (GrantedAuthority) a)
                .anyMatch(ga -> ga.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }

    @GetMapping("/getemail")
    public String getEmail() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return user.getEmailAddress();
    }
}
