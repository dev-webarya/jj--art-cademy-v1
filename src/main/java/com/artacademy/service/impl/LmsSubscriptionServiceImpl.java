package com.artacademy.service.impl;

import com.artacademy.dto.request.LmsSubscriptionRequestDto;
import com.artacademy.dto.response.LmsSubscriptionResponseDto;
import com.artacademy.entity.LmsStudentSubscription;
import com.artacademy.entity.User;
import com.artacademy.enums.SubscriptionStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.LmsSubscriptionMapper;
import com.artacademy.repository.LmsStudentSubscriptionRepository;
import com.artacademy.repository.UserRepository;
import com.artacademy.service.LmsSubscriptionService;
import lombok.RequiredArgsConstructor;
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
public class LmsSubscriptionServiceImpl implements LmsSubscriptionService {

    private final LmsStudentSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final LmsSubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public LmsSubscriptionResponseDto create(LmsSubscriptionRequestDto request) {
        // Check for duplicate subscription
        if (subscriptionRepository.existsByStudentIdAndSubscriptionMonthAndSubscriptionYear(
                request.getStudentId(), request.getSubscriptionMonth(), request.getSubscriptionYear())) {
            throw new IllegalArgumentException("Subscription already exists for this student and month/year");
        }

        // Get student info
        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        LmsStudentSubscription subscription = subscriptionMapper.toEntity(request);

        // Set student info
        subscription.setStudentName(student.getFirstName() + " " + student.getLastName());
        subscription.setStudentEmail(student.getEmail());
        subscription.setStudentPhone(student.getPhoneNumber());

        // Set date range
        YearMonth yearMonth = YearMonth.of(request.getSubscriptionYear(), request.getSubscriptionMonth());
        subscription.setStartDate(yearMonth.atDay(1));
        subscription.setEndDate(yearMonth.atEndOfMonth());

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setAttendedSessions(0);

        LmsStudentSubscription saved = subscriptionRepository.save(subscription);
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
    public LmsSubscriptionResponseDto getByStudentIdAndMonth(String studentId, Integer month, Integer year) {
        return subscriptionRepository.findByStudentIdAndSubscriptionMonthAndSubscriptionYear(studentId, month, year)
                .map(subscriptionMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No subscription found for student in " + month + "/" + year));
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
    public LmsSubscriptionResponseDto renewSubscription(String studentId) {
        LocalDate now = LocalDate.now();
        int nextMonth = now.getMonthValue() == 12 ? 1 : now.getMonthValue() + 1;
        int year = now.getMonthValue() == 12 ? now.getYear() + 1 : now.getYear();

        LmsSubscriptionRequestDto request = LmsSubscriptionRequestDto.builder()
                .studentId(studentId)
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

        List<LmsStudentSubscription> activeSubscriptions = subscriptionRepository
                .findBySubscriptionMonthAndSubscriptionYear(currentMonth - 1, currentYear);

        for (LmsStudentSubscription sub : activeSubscriptions) {
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
