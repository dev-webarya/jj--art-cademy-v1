package com.artacademy.repository;

import com.artacademy.entity.EmailOtp;
import com.artacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, UUID> {

    Optional<EmailOtp> findByUser(User user);

    Optional<EmailOtp> findByOtpAndUser_Email(String otp, String email); // Changed from User_Username
}