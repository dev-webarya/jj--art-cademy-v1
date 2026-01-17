package com.artacademy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "token_deny_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDenyList {

    @Id
    private String id;

    @Indexed(unique = true)
    private String jti; // The unique identifier of the JWT

    private Instant expiryDate; // The original expiry date of the token
}