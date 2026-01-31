package com.artacademy.service.impl;

import com.artacademy.dto.request.LmsSubscriptionRequestDto;
import com.artacademy.dto.response.LmsSubscriptionResponseDto;
import com.artacademy.entity.ClassEnrollment;
import com.artacademy.entity.LmsStudentSubscription;
import com.artacademy.enums.EnrollmentStatus;
import com.artacademy.enums.SubscriptionStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.LmsSubscriptionMapper;
import com.artacademy.repository.ClassEnrollmentRepository;
import com.artacademy.repository.LmsStudentSubscriptionRepository;
import com.artacademy.service.LmsSubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LmsSubscriptionServiceImpl implements LmsSubscriptionService {

    private final LmsStudentSubscriptionRepository subscriptionRepository;
    private final ClassEnrollmentRepository enrollmentRepository;
    private final LmsSubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public LmsSubscriptionResponseDto create(LmsSubscriptionRequestDto request) {
        // Validate enrollment exists and is APPROVED
        ClassEnrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + request.getEnrollmentId()));

        if (enrollment.getStatus() != EnrollmentStatus.APPROVED) {
            throw new IllegalArgumentException(
                    "Only APPROVED enrollments can have subscriptions. Current status: " + enrollment.getStatus());
        }

        // Check for duplicate subscription
        if (subscriptionRepository.existsByEnrollmentIdAndSubscriptionMonthAndSubscriptionYear(
                request.getEnrollmentId(), request.getSubscriptionMonth(), request.getSubscriptionYear())) {
            throw new IllegalArgumentException("Subscription already exists for this enrollment and month/year");
        }

        LmsStudentSubscription subscription = subscriptionMapper.toEntity(request);

        // Set enrollment info
        subscription.setEnrollmentId(enrollment.getId());
        subscription.setRollNo(enrollment.getRollNo());
        subscription.setStudentId(enrollment.getUserId());
        subscription.setStudentName(enrollment.getStudentName());
        subscription.setStudentEmail(enrollment.getStudentEmail());
        subscription.setStudentPhone(enrollment.getStudentPhone());

        // Set date range
        YearMonth yearMonth = YearMonth.of(request.getSubscriptionYear(), request.getSubscriptionMonth());
        subscription.setStartDate(yearMonth.atDay(1));
        subscription.setEndDate(yearMonth.atEndOfMonth());

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setAttendedSessions(0);

        LmsStudentSubscription saved = subscriptionRepository.save(subscription);
        log.info("Created subscription {} for enrollment {} (rollNo: {})", saved.getId(), enrollment.getId(),
                enrollment.getRollNo());
        return subscriptionMapper.toResponse(saved);
    }

    @Override
    public LmsSubscriptionResponseDto getById(String id) {
        return subscriptionMapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public LmsSubscriptionResponseDto update(String id, LmsSubscriptionRequestDto request) {
        LmsStudentSubscription subscription = findById(id);
        subscriptionMapper.updateEntity(request, subscription);
        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subscription not found: " + id);
        }
        subscriptionRepository.deleteById(id);
    }

    @Override
    public Page<LmsSubscriptionResponseDto> getAll(Pageable pageable) {
        return subscriptionRepository.findAll(pageable).map(subscriptionMapper::toResponse);
    }

    @Override
    public List<LmsSubscriptionResponseDto> getByStudentId(String studentId) {
        return subscriptionRepository.findByStudentId(studentId).stream()
                .map(subscriptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LmsSubscriptionResponseDto getActiveByStudentId(String studentId) {
        return subscriptionRepository.findByStudentIdAndStatus(studentId, SubscriptionStatus.ACTIVE)
                .map(subscriptionMapper::toResponse)
                .orElse(null);
    }

    @Override
    public LmsSubscriptionResponseDto getByEnrollmentIdAndMonth(String enrollmentId, Integer month, Integer year) {
        return subscriptionRepository
                .findByEnrollmentIdAndSubscriptionMonthAndSubscriptionYear(enrollmentId, month, year)
                .map(subscriptionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No subscription found for enrollment in " + month + "/" + year));
    }

    @Override
    public Page<LmsSubscriptionResponseDto> getActiveSubscriptions(Pageable pageable) {
        return subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE, pageable)
                .map(subscriptionMapper::toResponse);
    }

    @Override
    public List<LmsSubscriptionResponseDto> getByMonth(Integer month, Integer year) {
        return subscriptionRepository.findBySubscriptionMonthAndSubscriptionYear(month, year).stream()
                .map(subscriptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LmsSubscriptionResponseDto renewSubscription(String enrollmentId) {
        // Validate enrollment exists and is APPROVED
        ClassEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found: " + enrollmentId));

        if (enrollment.getStatus() != EnrollmentStatus.APPROVED) {
            throw new IllegalArgumentException("Only APPROVED enrollments can be renewed");
        }

        LocalDate now = LocalDate.now();
        int nextMonth = now.getMonthValue() == 12 ? 1 : now.getMonthValue() + 1;
        int year = now.getMonthValue() == 12 ? now.getYear() + 1 : now.getYear();

        LmsSubscriptionRequestDto request = LmsSubscriptionRequestDto.builder()
                .enrollmentId(enrollmentId)
                .subscriptionMonth(nextMonth)
                .subscriptionYear(year)
                .allowedSessions(8)
                .build();

        return create(request);
    }

    @Override
    @Transactional
    public LmsSubscriptionResponseDto cancelSubscription(String id) {
        LmsStudentSubscription subscription = findById(id);
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
    }

    @Override
    public List<LmsSubscriptionResponseDto> getOverLimitSubscriptions() {
        return subscriptionRepository.findByStatusAndAttendedSessionsGreaterThan(SubscriptionStatus.ACTIVE, 8)
                .stream()
                .map(subscriptionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void expireOldSubscriptions() {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // Get previous month's subscriptions
        int prevMonth = currentMonth == 1 ? 12 : currentMonth - 1;
        int prevYear = currentMonth == 1 ? currentYear - 1 : currentYear;

        List<LmsStudentSubscription> oldSubscriptions = subscriptionRepository
                .findBySubscriptionMonthAndSubscriptionYear(prevMonth, prevYear);

        for (LmsStudentSubscription sub : oldSubscriptions) {
            if (sub.getStatus() == SubscriptionStatus.ACTIVE) {
                sub.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(sub);
            }
        }
    }

    private LmsStudentSubscription findById(String id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + id));
    }
}
