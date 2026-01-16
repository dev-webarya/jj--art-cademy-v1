package com.artacademy.lms.service;

import com.artacademy.entity.User;
import com.artacademy.lms.dto.request.InstructorRequestDto;
import com.artacademy.lms.dto.response.InstructorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InstructorService {

    // Admin operations
    InstructorResponseDto createInstructor(InstructorRequestDto request);

    InstructorResponseDto updateInstructor(UUID id, InstructorRequestDto request);

    /**
     * Soft deletes instructor and unassigns from all batches.
     * Batches get status NEEDS_INSTRUCTOR.
     */
    void deleteInstructor(UUID id);

    InstructorResponseDto reactivateInstructor(UUID id);

    // Common operations
    InstructorResponseDto getById(UUID id);

    InstructorResponseDto getByUserId(UUID userId);

    InstructorResponseDto getMyProfile(User user);

    Page<InstructorResponseDto> getAll(Pageable pageable);

    Page<InstructorResponseDto> getActiveInstructors(Pageable pageable);

    // Stats
    long countBatchesNeedingInstructor();
}
