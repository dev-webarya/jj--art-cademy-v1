package com.artacademy.controller;

import com.artacademy.dto.request.*;
import com.artacademy.dto.response.AuthenticationResponse;
import com.artacademy.dto.response.RegistrationResponse;
import com.artacademy.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("User registration attempt for email: {}", request.getEmail());
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Development-only endpoint to register a user and immediately enable them,
     * bypassing the OTP verification step.
     */
    @PostMapping("/dev-register")
    public ResponseEntity<AuthenticationResponse> devRegister(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.devRegister(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthenticationResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("OTP verification attempt for email: {}", request.getEmail());
        return ResponseEntity.ok(authenticationService.verifyEmailOtp(request));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<RegistrationResponse> requestOtp(@Valid @RequestBody OtpRequest request) {
        return ResponseEntity.ok(authenticationService.requestOtp(request));
    }

    @PostMapping("/verify-login-otp")
    public ResponseEntity<AuthenticationResponse> verifyLoginOtp(@Valid @RequestBody LoginOtpRequest request) {
        return ResponseEntity.ok(authenticationService.verifyLoginOtp(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<RegistrationResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        return ResponseEntity.ok(authenticationService.resetPassword(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}