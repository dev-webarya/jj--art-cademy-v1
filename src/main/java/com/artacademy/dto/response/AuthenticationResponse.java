package com.artacademy.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    private String id;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresAt;
}