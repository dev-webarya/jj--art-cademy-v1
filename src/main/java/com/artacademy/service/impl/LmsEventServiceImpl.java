package com.artacademy.service.impl;

import com.artacademy.dto.request.LmsEventRequestDto;
import com.artacademy.dto.response.LmsEventResponseDto;
import com.artacademy.entity.LmsEvent;
import com.artacademy.entity.User;
import com.artacademy.enums.EventType;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.LmsEventMapper;
import com.artacademy.repository.LmsEventRepository;
import com.artacademy.service.LmsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LmsEventServiceImpl implements LmsEventService {

    private final LmsEventRepository eventRepository;
    private final LmsEventMapper eventMapper;

    @Override
    @Transactional
    public LmsEventResponseDto create(LmsEventRequestDto request) {
        LmsEvent event = eventMapper.toEntity(request);
        event.setCreatedBy(getCurrentUserId());
        event.setCurrentParticipants(0);
        event.setIsDeleted(false);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Override
    public LmsEventResponseDto getById(String id) {
        return eventMapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public LmsEventResponseDto update(String id, LmsEventRequestDto request) {
        LmsEvent event = findById(id);
        eventMapper.updateEntity(request, event);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public void delete(String id) {
        LmsEvent event = findById(id);
        event.setIsDeleted(true);
        eventRepository.save(event);
    }

    @Override
    public Page<LmsEventResponseDto> getAll(Pageable pageable) {
        return eventRepository.findByIsDeletedFalse(pageable).map(eventMapper::toResponse);
    }

    @Override
    public Page<LmsEventResponseDto> getByType(EventType eventType, Pageable pageable) {
        return eventRepository.findByEventTypeAndIsDeletedFalse(eventType, pageable)
                .map(eventMapper::toResponse);
    }

    @Override
    public Page<LmsEventResponseDto> getUpcoming(Pageable pageable) {
        return eventRepository.findByStartDateTimeGreaterThanEqualAndIsDeletedFalse(Instant.now(), pageable)
                .map(eventMapper::toResponse);
    }

    @Override
    public Page<LmsEventResponseDto> getPublicEvents(Pageable pageable) {
        return eventRepository.findByIsPublicTrueAndIsDeletedFalse(pageable)
                .map(eventMapper::toResponse);
    }

    private LmsEvent findById(String id) {
        return eventRepository.findById(id)
                .filter(e -> !e.getIsDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
    }

    private String getCurrentUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
