package com.artacademy.dto.response;

import com.artacademy.enums.ClassSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String rollNo;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Set<String> roles;
    private boolean isEnabled;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;

    // Student-specific fields
    private String parentGuardianName;
    private Integer studentAge;
    private ClassSchedule preferredSchedule;
    private String additionalMessage;
}
