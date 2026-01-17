package com.artacademy.lms.service.impl;

import com.artacademy.entity.User;
import com.artacademy.exception.InvalidRequestException;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.lms.dto.request.InstructorRequestDto;
import com.artacademy.lms.dto.response.InstructorResponseDto;
import com.artacademy.lms.entity.Instructor;
import com.artacademy.lms.enums.BatchStatus;
import com.artacademy.lms.mapper.LmsMapper;
import com.artacademy.lms.repository.BatchRepository;
import com.artacademy.lms.repository.InstructorRepository;
import com.artacademy.lms.service.InstructorService;
import com.artacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final BatchRepository batchRepository;
    private final UserRepository userRepository;
    private final LmsMapper mapper;

    @Override
    public InstructorResponseDto createInstructor(InstructorRequestDto request) {
        log.info("Creating instructor for user: {}", request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (instructorRepository.existsByUser(user)) {
            throw new InvalidRequestException("User is already an instructor");
        }

        Instructor instructor = Instructor.builder()
                .user(user)
                .bio(request.getBio())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .profileImageUrl(request.getProfileImageUrl())
                .isActive(request.isActive())
                .build();

        return mapper.toInstructorDto(instructorRepository.save(instructor));
    }

    @Override
    public InstructorResponseDto updateInstructor(UUID id, InstructorRequestDto request) {
        log.info("Updating instructor: {}", id);

        Instructor instructor = findById(id);
        instructor.setBio(request.getBio());
        instructor.setSpecialization(request.getSpecialization());
        instructor.setQualification(request.getQualification());
        instructor.setProfileImageUrl(request.getProfileImageUrl());
        instructor.setActive(request.isActive());

        return mapper.toInstructorDto(instructorRepository.save(instructor));
    }

    @Override
    public void deleteInstructor(UUID id) {
        log.warn("Deleting instructor: {} - will unassign from all batches", id);

        Instructor instructor = findById(id);

        // First, unassign instructor from all their batches
        int unassignedCount = batchRepository.unassignInstructorFromBatches(id, BatchStatus.NEEDS_INSTRUCTOR);
        log.info("Unassigned instructor from {} batches", unassignedCount);

        // Soft delete the instructor
        instructorRepository.delete(instructor);
        log.info("Instructor {} deleted (soft)", id);
    }

    @Override
    public InstructorResponseDto reactivateInstructor(UUID id) {
        log.info("Reactivating instructor: {}", id);
        // Note: This would require a native query since soft-deleted records are
        // filtered
        throw new UnsupportedOperationException("Reactivation requires native query - to be implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponseDto getById(UUID id) {
        return mapper.toInstructorDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponseDto getByUserId(UUID userId) {
        Instructor instructor = instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found for user"));
        return mapper.toInstructorDto(instructor);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponseDto getMyProfile(User user) {
        Instructor instructor = instructorRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("You are not registered as an instructor"));
        return mapper.toInstructorDto(instructor);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstructorResponseDto> getAll(Pageable pageable) {
        return instructorRepository.findAll(pageable).map(mapper::toInstructorDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InstructorResponseDto> getActiveInstructors(Pageable pageable) {
        return instructorRepository.findByIsActiveTrue(pageable).map(mapper::toInstructorDto);
    }

    @Override
    @Transactional(readOnly = true)
    public long countBatchesNeedingInstructor() {
        return batchRepository.countByStatus(BatchStatus.NEEDS_INSTRUCTOR);
    }

    private Instructor findById(UUID id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
    }
}
