package com.artacademy.config;

import com.artacademy.entity.*;
import com.artacademy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                log.info("--- Starting Application Data Seeding ---");

                // 1. Initialize Essential Roles (System Foundation)
                Map<String, Role> roles = seedRoles();

                // 2. Check if Admin exists. If yes, assume system is already initialized.
                if (userRepository.findByEmail("admin@artacademy.com").isPresent()) {
                        log.info("System already seeded. Skipping initialization.");
                        return;
                }

                // 3. Create Super Admin
                seedAdmin(roles.get("ROLE_ADMIN"));

                // 4. Onboard Sample Customers
                seedCustomers(roles.get("ROLE_CUSTOMER"));

                log.info("--- Data Seeding Completed Successfully ---");
        }

        private Map<String, Role> seedRoles() {
                log.info("[Phase 1] Seeding Roles...");
                List<String> roleNames = List.of("ROLE_CUSTOMER", "ROLE_ADMIN");
                return roleNames.stream()
                                .map(name -> roleRepository.findByName(name)
                                                .orElseGet(() -> roleRepository
                                                                .save(Role.builder().name(name).build())))
                                .collect(Collectors.toMap(Role::getName, role -> role));
        }

        private User seedAdmin(Role adminRole) {
                log.info("[Phase 2] Seeding Super Admin...");
                return userRepository.save(User.builder()
                                .firstName("Super")
                                .lastName("Admin")
                                .email("admin@artacademy.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("0000000000")
                                .roles(new HashSet<>(Set.of(adminRole)))
                                .isEnabled(true)
                                .build());
        }

        private void seedCustomers(Role customerRole) {
                log.info("[Phase 3] Onboarding Customers...");
                userRepository.save(User.builder()
                                .firstName("Alice")
                                .lastName("Artist")
                                .email("alice@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231234")
                                .roles(new HashSet<>(Set.of(customerRole)))
                                .isEnabled(true)
                                .build());

                userRepository.save(User.builder()
                                .firstName("Bob")
                                .lastName("Collector")
                                .email("bob@test.com")
                                .password(passwordEncoder.encode("Password@123"))
                                .phoneNumber("1231231235")
                                .roles(new HashSet<>(Set.of(customerRole)))
                                .isEnabled(true)
                                .build());
        }
}