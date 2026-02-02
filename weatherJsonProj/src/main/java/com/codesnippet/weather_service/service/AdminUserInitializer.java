package com.codesnippet.weather_service.service;


import com.codesnippet.weather_service.entity.Users;
import com.codesnippet.weather_service.repository.UserDetailsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner createAdminUser(UserDetailsRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin1234")); 
                admin.setRole("ROLE_ADMIN");
                // Securely store password

                userRepository.save(admin);
                System.out.println("Default admin user created!");
            }
            if (userRepository.findByUsername("user").isEmpty()) {
                Users admin = new Users();
                admin.setUsername("user");
                admin.setPassword(passwordEncoder.encode("user1234")); // Securely store password
                admin.setRole("ROLE_USER");
                userRepository.save(admin);
                System.out.println("Default admin user created!");
            }
        };
    }
}
