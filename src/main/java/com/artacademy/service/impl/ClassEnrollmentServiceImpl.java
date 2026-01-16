package com.artacademy.service.impl;

import com.artacademy.dto.request.ClassEnrollmentRequestDto;
import com.artacademy.dto.request.EnrollmentStatusUpdateDto;
import com.artacademy.dto.response.ClassEnrollmentResponseDto;
import com.artacademy.entity.ArtClasses;
import com.artacademy.entity.ClassEnrollment;
import com.artacademy.entity.User;
import com.artacademy.enums.EnrollmentStatus;
import com.artacademy.exception.InvalidRequestException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClassEnrollmentServiceImpl implements ClassEnrollmentService {

    private final ClassEnrollmentRepository enrollmentRepository;
    private final ArtClassesRepository artClassesRepository;
    private final ClassEnrollmentMapper enrollmentMapper;

    @Override
    public ClassEnrollmentResponseDto enroll(ClassEnrollmentRequestDto request, User user) {
        log.info("User {} enrolling in class {}", user.getEmail(), request.getClassId());

        // Find the class
        ArtClasses artClass = artClassesRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with ID: " + request.getClassId()));

        // Check if user already enrolled in this class
        if (enrollmentRepository.existsByUserAndArtClass(user, artClass)) {
            throw new InvalidRequestException("You are already enrolled in this class");
        }

        // Create enrollment
        ClassEnrollment enrollment = ClassEnrollment.builder()
                .user(user)
                .artClass(artClass)
                .parentGuardianName(request.getParentGuardianName())
                .studentAge(request.getStudentAge())
                .schedule(request.getSchedule())
                .additionalMessage(request.getAdditionalMessage())
                .status(EnrollmentStatus.PENDING)
                .build();

        ClassEnrollment saved = enrollmentRepository.save(enrollment);
        log.info("Enrollment created with ID: {} and status: PENDING", saved.getId());

        return enrollmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassEnrollmentResponseDto> getMyEnrollments(User user, Pageable pageable) {
        log.info("Fetching enrollments for user: {}", user.getEmail());
        return enrollmentRepository.findByUser(user, pageable)
                .map(enrollmentMapper::toResponseDto);
    }

    @Override
    public ClassEnrollmentResponseDto cancelEnrollment(UUID enrollmentId, User user) {
        log.info("User {} cancelling enrollment {}", user.getEmail(), enrollmentId);

        ClassEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + enrollmentId));

        // Verify ownership
        if (!enrollment.getUser().getId().equals(user.getId())) {
            throw new InvalidRequestException("You can only cancel your own enrollments");
        }

        // Check if already approved
        if (enrollment.getStatus() == EnrollmentStatus.APPROVED) {
            throw new InvalidRequestException("Cannot cancel approved enrollments. Please contact admin.");
        }

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        ClassEnrollment saved = enrollmentRepository.save(enrollment);

        log.info("Enrollment {} cancelled", enrollmentId);
        return enrollmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassEnrollmentResponseDto> getAllEnrollments(Pageable pageable) {
        log.info("Fetching all enrollments");
        return enrollmentRepository.findAll(pageable)
                .map(enrollmentMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassEnrollmentResponseDto> getPendingEnrollments(Pageable pageable) {
        log.info("Fetching pending enrollments");
        return enrollmentRepository.findByStatus(EnrollmentStatus.PENDING, pageable)
                .map(enrollmentMapper::toResponseDto);
    }

    @Override
    public ClassEnrollmentResponseDto updateEnrollmentStatus(UUID enrollmentId, EnrollmentStatusUpdateDto request) {
        log.info("Updating enrollment {} status to {}", enrollmentId, request.getStatus());

        ClassEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + enrollmentId));

        enrollment.setStatus(request.getStatus());
        enrollment.setAdminNotes(request.getAdminNotes());

        ClassEnrollment saved = enrollmentRepository.save(enrollment);
        log.info("Enrollment {} updated to status: {}", enrollmentId, request.getStatus());

        return enrollmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassEnrollmentResponseDto getEnrollmentById(UUID enrollmentId) {
        ClassEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + enrollmentId));
        return enrollmentMapper.toResponseDto(enrollment);
    }

    @Override
    public void deleteEnrollment(UUID enrollmentId) {
        log.warn("Deleting enrollment {}", enrollmentId);
        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new ResourceNotFoundException("Enrollment not found with ID: " + enrollmentId);
        }
        enrollmentRepository.deleteById(enrollmentId);
        log.info("Enrollment {} deleted", enrollmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPendingEnrollments() {
        return enrollmentRepository.countByStatus(EnrollmentStatus.PENDING);
    }
}
