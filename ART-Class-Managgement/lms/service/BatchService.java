package com.artacademy.lms.service;

import com.artacademy.entity.User;
import com.artacademy.lms.dto.request.BatchRequestDto;
import com.artacademy.lms.dto.response.BatchResponseDto;
import com.artacademy.lms.dto.response.BatchStudentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BatchService {

    // CRUD
    BatchResponseDto create(BatchRequestDto request);

    BatchResponseDto update(UUID id, BatchRequestDto request);

    BatchResponseDto getById(UUID id);

    Page<BatchResponseDto> getAll(Pageable pageable);

    void delete(UUID id);

    // Instructor operations
    BatchResponseDto assignInstructor(UUID batchId, UUID instructorId);

    Page<BatchResponseDto> getByInstructor(UUID instructorId, Pageable pageable);

    Page<BatchResponseDto> getMyBatches(User instructorUser, Pageable pageable);

    // Student management
    BatchStudentResponseDto addStudent(UUID batchId, UUID userId, UUID enrollmentId);

    void removeStudent(UUID batchId, UUID userId);

    List<BatchStudentResponseDto> getStudents(UUID batchId);

    // For students to see their batches
    Page<BatchResponseDto> getStudentBatches(User user, Pageable pageable);

    // Admin views
    Page<BatchResponseDto> getBatchesNeedingInstructor(Pageable pageable);
}
