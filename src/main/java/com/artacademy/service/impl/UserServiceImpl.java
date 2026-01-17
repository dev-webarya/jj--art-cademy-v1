package com.artacademy.service.impl;

import com.artacademy.dto.request.UserRequest;
import com.artacademy.dto.response.UserResponse;
import com.artacademy.entity.Role;
import com.artacademy.entity.User;
import com.artacademy.exception.DuplicateResourceException;
import com.artacademy.exception.InvalidRequestException;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.UserMapper;
import com.artacademy.repository.RoleRepository;
import com.artacademy.repository.UserRepository;
import com.artacademy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating user with email: {}", userRequest.getEmail());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Uniqueness Checks
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicateResourceException("User with email '" + userRequest.getEmail() + "' already exists.");
        }
        if (userRequest.getPhoneNumber() != null
                && userRepository.findByPhoneNumber(userRequest.getPhoneNumber()).isPresent()) {
            throw new DuplicateResourceException(
                    "User with phone '" + userRequest.getPhoneNumber() + "' already exists.");
        }

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhoneNumber(userRequest.getPhoneNumber());

        // Role Assignment Logic
        if (userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            Role customerRole = findRoleByName("ROLE_CUSTOMER");
            user.getRoles().add(User.RoleRef.builder()
                    .roleId(customerRole.getId())
                    .name(customerRole.getName())
                    .build());
        } else {
            // Restriction: Only Admin can assign Admin role
            boolean tryingToAssignPrivileged = userRequest.getRoles().stream()
                    .anyMatch(r -> r.contains("ADMIN"));

            if (tryingToAssignPrivileged && !isAdmin) {
                throw new AccessDeniedException("Only Admins can create other Admins.");
            }

            Set<User.RoleRef> roleRefs = userRequest.getRoles().stream()
                    .map(roleName -> {
                        Role role = findRoleByName(roleName);
                        return User.RoleRef.builder()
                                .roleId(role.getId())
                                .name(role.getName())
                                .build();
                    })
                    .collect(Collectors.toSet());
            user.setRoles(roleRefs);
        }

        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        log.debug("User created with ID: {}", savedUser.getId());
        return userMapper.toResponse(savedUser);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable, String searchTerm) {
        Page<User> page = userRepository.findAll(pageable);
        List<UserResponse> users = page.getContent().stream()
                .filter(u -> !u.isDeleted())
                .filter(u -> searchTerm == null || searchTerm.isEmpty() ||
                        u.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        u.getLastName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        u.getEmail().toLowerCase().contains(searchTerm.toLowerCase()))
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, page.getTotalElements());
    }

    @Override
    public UserResponse getUserById(String userId) {
        User user = findUserById(userId);
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(String userId, UserRequest userRequest) {
        User existingUser = findUserById(userId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Validation: Unique Email/Phone
        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail()))
                throw new DuplicateResourceException("Email exists");
            existingUser.setEmail(userRequest.getEmail());
        }

        userMapper.updateEntityFromRequest(userRequest, existingUser);

        // Update Password
        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        // Update Roles (Admin Only)
        if (userRequest.getRoles() != null && isAdmin) {
            Set<User.RoleRef> roleRefs = userRequest.getRoles().stream()
                    .map(roleName -> {
                        Role role = findRoleByName(roleName);
                        return User.RoleRef.builder()
                                .roleId(role.getId())
                                .name(role.getName())
                                .build();
                    })
                    .collect(Collectors.toSet());
            existingUser.setRoles(roleRefs);
        }

        return userMapper.toResponse(userRepository.save(existingUser));
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        return userMapper.toResponse(currentUser);
    }

    @Override
    public UserResponse updateCurrentUser(UserRequest userRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        // Safe Updates (Name, Phone, Password)
        if (userRequest.getFirstName() != null && !userRequest.getFirstName().isBlank()) {
            currentUser.setFirstName(userRequest.getFirstName());
        }

        if (userRequest.getLastName() != null && !userRequest.getLastName().isBlank()) {
            currentUser.setLastName(userRequest.getLastName());
        }

        // Phone Update with Uniqueness Check
        if (userRequest.getPhoneNumber() != null
                && !userRequest.getPhoneNumber().equals(currentUser.getPhoneNumber())) {
            userRepository.findByPhoneNumber(userRequest.getPhoneNumber()).ifPresent(u -> {
                throw new DuplicateResourceException(
                        "Phone number '" + userRequest.getPhoneNumber() + "' already exists.");
            });
            currentUser.setPhoneNumber(userRequest.getPhoneNumber());
        }

        // Password Update
        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            currentUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        // Roles are deliberately IGNORED here for security.

        return userMapper.toResponse(userRepository.save(currentUser));
    }

    @Override
    public void deleteUser(String userId) {
        log.warn("Deleting user ID: {}", userId);
        User user = findUserById(userId);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getUsersByRole(String roleName) {
        return userRepository.findByRoleName(roleName).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new InvalidRequestException("Role not found: " + roleName));
    }
}