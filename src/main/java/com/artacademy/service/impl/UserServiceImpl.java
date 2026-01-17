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
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
    private final MongoTemplate mongoTemplate;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating user with email: {}", userRequest.getEmail());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // 2. Uniqueness Checks - Handled by @Indexed(unique=true) but good to check
        // normally too
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

        // 3. Role Assignment Logic
        if (userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            // Default role
            Role role = findRoleEntityByName("ROLE_CUSTOMER");
            user.getRoles().add(role.getName());
        } else {
            // Restriction: Only Admin can assign Admin role
            boolean tryingToAssignPrivileged = userRequest.getRoles().stream()
                    .anyMatch(r -> r.contains("ADMIN"));

            if (tryingToAssignPrivileged && !isAdmin) {
                throw new AccessDeniedException("Only Admins can create other Admins.");
            }

            // Verify roles exist
            Set<String> validRoleNames = new HashSet<>();
            for (String roleName : userRequest.getRoles()) {
                Role role = findRoleEntityByName(roleName);
                validRoleNames.add(role.getName());
            }
            user.setRoles(validRoleNames);
        }

        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        log.debug("User created with ID: {}", savedUser.getId());
        return userMapper.toResponse(savedUser);
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable, String searchTerm) {
        Query query = new Query().with(pageable);

        if (searchTerm != null && !searchTerm.isBlank()) {
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("firstName").regex(searchTerm, "i"),
                    Criteria.where("lastName").regex(searchTerm, "i"),
                    Criteria.where("email").regex(searchTerm, "i"));
            query.addCriteria(criteria);
        }

        List<User> users = mongoTemplate.find(query, User.class);
        return PageableExecutionUtils.getPage(users, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(0).skip(0), User.class))
                .map(userMapper::toResponse);
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
        boolean isAdmin = auth != null
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Validation: Unique Email/Phone
        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail()))
                throw new DuplicateResourceException("Email exists");
        }

        userMapper.updateEntityFromRequest(userRequest, existingUser);

        // Update Password
        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        // Update Roles (Admin Only)
        if (userRequest.getRoles() != null && isAdmin) {
            Set<String> validRoleNames = new HashSet<>();
            for (String roleName : userRequest.getRoles()) {
                Role role = findRoleEntityByName(roleName);
                validRoleNames.add(role.getName());
            }
            existingUser.setRoles(validRoleNames);
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

        if (userRequest.getFirstName() != null && !userRequest.getFirstName().isBlank()) {
            currentUser.setFirstName(userRequest.getFirstName());
        }

        if (userRequest.getLastName() != null && !userRequest.getLastName().isBlank()) {
            currentUser.setLastName(userRequest.getLastName());
        }

        if (userRequest.getPhoneNumber() != null
                && !userRequest.getPhoneNumber().equals(currentUser.getPhoneNumber())) {
            userRepository.findByPhoneNumber(userRequest.getPhoneNumber()).ifPresent(u -> {
                throw new DuplicateResourceException(
                        "Phone number '" + userRequest.getPhoneNumber() + "' already exists.");
            });
            currentUser.setPhoneNumber(userRequest.getPhoneNumber());
        }

        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            currentUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        return userMapper.toResponse(userRepository.save(currentUser));
    }

    @Override
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        userRepository.deleteById(userId);
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private Role findRoleEntityByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new InvalidRequestException("Role not found: " + roleName));
    }
}