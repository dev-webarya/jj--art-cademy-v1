package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "email_otps")
public class EmailOtp {

    @Id
    private String id;

    @Indexed
    private String email;

    private String otp;

    @Indexed(expireAfter = "0s")
    private Instant expiryDate;

    private String userId;

    @Builder.Default
    private boolean verified = false;

    @CreatedDate
    private Instant createdAt;
}