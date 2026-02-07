package com.artacademy.service.impl;

import com.artacademy.dto.request.LmsClassSessionRequestDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import com.artacademy.entity.LmsClassSession;
import com.artacademy.enums.SessionStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.LmsClassSessionMapper;
import com.artacademy.repository.LmsClassSessionRepository;
import com.artacademy.service.LmsClassSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LmsClassSessionServiceImpl implements LmsClassSessionService {

    private final LmsClassSessionRepository sessionRepository;
    private final LmsClassSessionMapper sessionMapper;

    @Override
    @Transactional
    public LmsClassSessionResponseDto create(LmsClassSessionRequestDto request) {
        LmsClassSession session = sessionMapper.toEntity(request);
        session.setStatus(SessionStatus.SCHEDULED);
        return sessionMapper.toResponse(sessionRepository.save(session));
    }

    @Override
    public LmsClassSessionResponseDto getById(String id) {
        return sessionMapper.toResponse(findById(id));
    }

    @Override
    @Transactional
    public LmsClassSessionResponseDto update(String id, LmsClassSessionRequestDto request) {
        LmsClassSession session = findById(id);
        sessionMapper.updateEntity(request, session);
        return sessionMapper.toResponse(sessionRepository.save(session));
    }

    @Override
    @Transactional
    public void delete(String id) {
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Class session not found: " + id);
        }
        sessionRepository.deleteById(id);
    }

    @Override
    public Page<LmsClassSessionResponseDto> getAll(Pageable pageable) {
        return sessionRepository.findAll(pageable).map(sessionMapper::toResponse);
    }

    @Override
    public List<LmsClassSessionResponseDto> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return sessionMapper.toResponseList(
                sessionRepository.findBySessionDateBetween(startDate, endDate));
    }

    @Override
    public List<LmsClassSessionResponseDto> getByDate(LocalDate date) {
        return sessionMapper.toResponseList(sessionRepository.findBySessionDate(date));
    }

    @Override
    public Page<LmsClassSessionResponseDto> getUpcoming(Pageable pageable) {
        return sessionRepository.findBySessionDateGreaterThanEqual(LocalDate.now(), pageable)
                .map(sessionMapper::toResponse);
    }

    @Override
    @Transactional
    public LmsClassSessionResponseDto updateStatus(String id, SessionStatus status, String reason) {
        LmsClassSession session = findById(id);
        session.setStatus(status);
        if (status == SessionStatus.CANCELLED && reason != null) {
            session.setCancellationReason(reason);
        }
        return sessionMapper.toResponse(sessionRepository.save(session));
    }

    @Override
    public LmsClassSessionResponseDto getSessionWithAttendance(String id) {
        return sessionMapper.toResponse(findById(id));
    }

    private LmsClassSession findById(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class session not found: " + id));
    }
}
