package com.artacademy.repository;

import com.artacademy.entity.EmailOtp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailOtpRepository extends MongoRepository<EmailOtp, String> {

    Optional<EmailOtp> findByEmail(String email);

    Optional<EmailOtp> findByEmailAndOtp(String email, String otp);

    void deleteByEmail(String email);
}