package com.artacademy.controller;

import com.artacademy.dto.request.UserRequest;
import com.artacademy.dto.response.UserResponse;
import com.artacademy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // --- NEW: Get Current User Profile ---
    // Accessible by anyone logged in (Customer, Manager, Admin)
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    // --- NEW: Authenticated User Self-Update ---
    // Accessible by anyone logged in (Customer, Manager, Admin)
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }
    // -------------------------------------------

    // Admin/Manager creating other users
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        UserResponse createdUser = userService.createUser(request);
        log.debug("User created with ID: {}", createdUser.getId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable, search));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") String userId,
            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String userId) {
        log.warn("Deleting user ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}