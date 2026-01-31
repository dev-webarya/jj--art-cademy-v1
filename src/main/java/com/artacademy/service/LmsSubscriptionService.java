package com.artacademy.service;

import com.artacademy.dto.request.LmsSubscriptionRequestDto;
import com.artacademy.dto.response.LmsSubscriptionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service for managing student subscriptions.
 * Admin-only operations.
 */
public interface LmsSubscriptionService {

    // CRUD operations
    LmsSubscriptionResponseDto create(LmsSubscriptionRequestDto request);

    LmsSubscriptionResponseDto getById(String id);

    LmsSubscriptionResponseDto update(String id, LmsSubscriptionRequestDto request);

    void delete(String id);

    // Query operations
    Page<LmsSubscriptionResponseDto> getAll(Pageable pageable);

    List<LmsSubscriptionResponseDto> getByStudentId(String studentId);

    LmsSubscriptionResponseDto getActiveByStudentId(String studentId);

    LmsSubscriptionResponseDto getByStudentIdAndMonth(String studentId, Integer month, Integer year);

    Page<LmsSubscriptionResponseDto> getActiveSubscriptions(Pageable pageable);

    List<LmsSubscriptionResponseDto> getByMonth(Integer month, Integer year);

    // Subscription management
    LmsSubscriptionResponseDto renewSubscription(String studentId);

    LmsSubscriptionResponseDto cancelSubscription(String id);

    List<LmsSubscriptionResponseDto> getOverLimitSubscriptions();

    // Monthly operations
    void expireOldSubscriptions();
}
