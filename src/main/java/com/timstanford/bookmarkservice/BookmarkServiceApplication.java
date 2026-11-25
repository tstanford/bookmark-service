package com.timstanford.bookmarkservice;

import com.timstanford.bookmarkservice.data.User;
import com.timstanford.bookmarkservice.data.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@SpringBootApplication
@EnableAsync
public class BookmarkServiceApplication implements CommandLineRunner {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{};:,.?";
    private static final String PASSWORD_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SYMBOLS;
    private static final int PASSWORD_LENGTH = 16;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BookmarkServiceApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
		SpringApplication.run(BookmarkServiceApplication.class, args);
	}

    static String generatePassword() {
        char[] password = new char[PASSWORD_LENGTH];
        password[0] = randomCharacter(LOWERCASE);
        password[1] = randomCharacter(UPPERCASE);
        password[2] = randomCharacter(DIGITS);
        password[3] = randomCharacter(SYMBOLS);

        for (int i = 4; i < password.length; i++) {
            password[i] = randomCharacter(PASSWORD_CHARACTERS);
        }

        for (int i = password.length - 1; i > 0; i--) {
            int swapIndex = SECURE_RANDOM.nextInt(i + 1);
            char current = password[i];
            password[i] = password[swapIndex];
            password[swapIndex] = current;
        }

        return new String(password);
    }

    private static char randomCharacter(String characters) {
        return characters.charAt(SECURE_RANDOM.nextInt(characters.length()));
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findByUsernameIgnoreCase("admin").isEmpty()){
            String password = generatePassword();
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode(password));
            adminUser.setAdmin(true);
            adminUser.setEmailAddress("admin@timcloud.uk");
            userRepository.save(adminUser);
            System.out.println("Admin user created with generated password: " + password);
        }
    }
}
