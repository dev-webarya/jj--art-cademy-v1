package com.artacademy.dto.request;

import com.artacademy.enums.ClassSchedule;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    // Password optional for updates
    private String password;

    private Set<String> roles;

    // Student-specific fields
    private String parentGuardianName;

    @Min(value = 3, message = "Student age must be at least 3")
    @Max(value = 100, message = "Student age must be at most 100")
    private Integer studentAge;

    private ClassSchedule preferredSchedule;

    private String additionalMessage;
}
