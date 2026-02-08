package com.darpan.realtimemultiplayerquiz.config;

import com.darpan.realtimemultiplayerquiz.dao.UserRepository;
import com.darpan.realtimemultiplayerquiz.model.Role;
import com.darpan.realtimemultiplayerquiz.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("password")); // Default password
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole(Role.ADMIN);
            admin.setMfaEnabled(false);
            admin.setEmailVerified(true); // Auto-verified
            admin.setAccountLocked(false);

            userRepository.save(admin);
            System.out.println("Admin user created: admin@test.com / password");
        }
    }
}
