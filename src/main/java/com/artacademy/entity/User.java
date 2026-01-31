package com.artacademy.entity;

import com.artacademy.enums.ClassSchedule;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    // Unique roll number for students (e.g., "AA-0001")
    @Indexed(unique = true, sparse = true)
    private String rollNo;

    private String firstName;

    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String password;

    @Indexed(unique = true, sparse = true)
    private String phoneNumber;

    @Builder.Default
    private boolean isEnabled = false;

    // Soft Delete Flag
    @Builder.Default
    private boolean deleted = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    // Store role names directly instead of @ManyToMany
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    // Student-specific fields
    private String parentGuardianName;

    private Integer studentAge;

    private ClassSchedule preferredSchedule;

    private String additionalMessage;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}