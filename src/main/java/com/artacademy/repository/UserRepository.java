package com.artacademy.repository;

import com.artacademy.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    // Find users by role name
    @Query("{'roles.name': ?0, 'deleted': false}")
    List<User> findByRoleName(String roleName);

    // Find all non-deleted users
    List<User> findByDeletedFalse();

    // Find by email ignoring deleted status (for auth)
    @Query("{'email': ?0}")
    Optional<User> findByEmailIncludingDeleted(String email);
}