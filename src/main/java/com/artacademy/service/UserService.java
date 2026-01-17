package com.artacademy.service;

import com.artacademy.dto.request.UserRequest;
import com.artacademy.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);

    Page<UserResponse> getAllUsers(Pageable pageable, String searchTerm);

    UserResponse getUserById(String userId);

    UserResponse updateUser(String userId, UserRequest userRequest);

    // --- Self-Management Methods ---
    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UserRequest userRequest);

    void deleteUser(String userId);

    // --- MongoDB specific ---
    List<UserResponse> getUsersByRole(String roleName);
}