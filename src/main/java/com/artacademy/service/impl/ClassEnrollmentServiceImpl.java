package com.artacademy.service.impl;

import com.artacademy.dto.request.ClassEnrollmentRequestDto;
import com.artacademy.dto.request.EnrollmentStatusUpdateDto;
import com.artacademy.dto.response.ClassEnrollmentResponseDto;
import com.artacademy.entity.ArtClasses;
import com.artacademy.entity.ClassEnrollment;
import com.artacademy.entity.User;
import com.artacademy.enums.EnrollmentStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ClassEnrollmentMapper;
import com.artacademy.repository.ArtClassesRepository;
import com.artacademy.repository.ClassEnrollmentRepository;
import com.artacademy.service.ClassEnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassEnrollmentServiceImpl implements ClassEnrollmentService {

    private final ClassEnrollmentRepository enrollmentRepository;
    private final ArtClassesRepository classRepository;
    private final ClassEnrollmentMapper enrollmentMapper;

    @Override
    public ClassEnrollmentResponseDto enroll(ClassEnrollmentRequestDto request, User user) {
        log.info("Enrolling user {} in class {}", user.getId(), request.getClassId());

        ArtClasses artClass = classRepository.findByIdAndDeletedFalse(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtClass", "id", request.getClassId()));

        ClassEnrollment enrollment = enrollmentMapper.toEntity(request);

        // Set references
        enrollment.setUserId(user.getId());
        // Note: studentEmail is populated from request via Mapper
        
        // Populate class details
        enrollment.setClassId(artClass.getId());
        enrollment.setClassName(artClass.getName());
        enrollment.setClassDescription(artClass.getDescription());

        enrollment.setStatus(EnrollmentStatus.PENDING);

        ClassEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(savedEnrollment);
    }

    @Override
    public Page<ClassEnrollmentResponseDto> getMyEnrollments(User user, Pageable pageable) {
        return enrollmentRepository.findByUserId(user.getId(), pageable)
                .map(enrollmentMapper::toDto);
    }

    @Override
    public ClassEnrollmentResponseDto cancelEnrollment(String enrollmentId, User user) {
        ClassEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        if (!enrollment.getUserId().equals(user.getId())) {
            throw new com.artacademy.exception.AccessDeniedException("You can only cancel your own enrollments");
        }

        if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed enrollment");
        }

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public Page<ClassEnrollmentResponseDto> getAllEnrollments(Pageable pageable) {
        return enrollmentRepository.findAll(pageable)
                .map(enrollmentMapper::toDto);
    }

    @Override
    public Page<ClassEnrollmentResponseDto> getPendingEnrollments(Pageable pageable) {
        return enrollmentRepository.findByStatus(EnrollmentStatus.PENDING, pageable)
                .map(enrollmentMapper::toDto);
    }

    @Override
    public ClassEnrollmentResponseDto updateEnrollmentStatus(String enrollmentId, EnrollmentStatusUpdateDto request) {
        ClassEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        enrollment.setStatus(request.getStatus());
        if (request.getAdminNotes() != null) {
            enrollment.setAdminNotes(request.getAdminNotes());
        }

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public ClassEnrollmentResponseDto getEnrollmentById(String enrollmentId) {
        ClassEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));
        return enrollmentMapper.toDto(enrollment);
    }

    @Override
    public void deleteEnrollment(String enrollmentId) {
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new ResourceNotFoundException("Enrollment", "id", enrollmentId);
        }
        enrollmentRepository.deleteById(enrollmentId);
    }

    @Override
    public long countPendingEnrollments() {
        return enrollmentRepository.countByStatus(EnrollmentStatus.PENDING);
    }
}
