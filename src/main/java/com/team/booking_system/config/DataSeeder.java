package com.team.booking_system.config;

import com.team.booking_system.entity.Resource;
import com.team.booking_system.entity.Role;
import com.team.booking_system.entity.User;
import com.team.booking_system.repository.ResourceRepository;
import com.team.booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seed() {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                userRepository.save(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .enabled(true)
                        .build());
                log.info("Seeded admin user");
            }

            if (!userRepository.existsByUsername("user")) {
                userRepository.save(User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .role(Role.USER)
                        .enabled(true)
                        .build());
                log.info("Seeded regular user");
            }

            if (resourceRepository.count() == 0) {
                resourceRepository.save(Resource.builder()
                        .name("Conference Room A").type("ROOM")
                        .description("10-person conference room").capacity(10).active(true).build());
                resourceRepository.save(Resource.builder()
                        .name("Toyota HiAce").type("VEHICLE")
                        .description("12-seat minivan").capacity(12).active(true).build());
                resourceRepository.save(Resource.builder()
                        .name("Projector #1").type("EQUIPMENT")
                        .description("4K laser projector").capacity(null).active(true).build());
                log.info("Seeded 3 resources");
            }
        };
    }
}