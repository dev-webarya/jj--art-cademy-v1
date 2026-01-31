package com.artacademy.service.impl;

import com.artacademy.dto.request.LmsAttendanceRequestDto;
import com.artacademy.dto.response.LmsAttendanceResponseDto;
import com.artacademy.dto.response.LmsClassSessionResponseDto;
import com.artacademy.entity.LmsAttendance;
import com.artacademy.entity.LmsClassSession;
import com.artacademy.entity.LmsStudentSubscription;
import com.artacademy.entity.User;
import com.artacademy.enums.AttendanceStatus;
import com.artacademy.enums.SubscriptionStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.LmsAttendanceMapper;
import com.artacademy.mapper.LmsClassSessionMapper;
import com.artacademy.repository.LmsAttendanceRepository;
import com.artacademy.repository.LmsClassSessionRepository;
import com.artacademy.repository.LmsStudentSubscriptionRepository;
import com.artacademy.repository.UserRepository;
import com.artacademy.service.LmsAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LmsAttendanceServiceImpl implements LmsAttendanceService {

    private final LmsAttendanceRepository attendanceRepository;
    private final LmsClassSessionRepository sessionRepository;
    private final LmsStudentSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final LmsAttendanceMapper attendanceMapper;
    private final LmsClassSessionMapper sessionMapper;

    @Override
    @Transactional
    public LmsClassSessionResponseDto markAttendance(LmsAttendanceRequestDto request) {
        LmsClassSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + request.getSessionId()));

        int month = session.getSessionDate().getMonthValue();
        int year = session.getSessionDate().getYear();

        int presentCount = 0;
        int absentCount = 0;
        List<LmsClassSession.SessionAttendanceRecord> embeddedRecords = new ArrayList<>();

        for (LmsAttendanceRequestDto.StudentAttendanceDto studentAttendance : request.getAttendanceList()) {
            User student = userRepository.findById(studentAttendance.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Student not found: " + studentAttendance.getStudentId()));

            // Find active subscription
            LmsStudentSubscription subscription = subscriptionRepository
                    .findByStudentIdAndStatus(studentAttendance.getStudentId(), SubscriptionStatus.ACTIVE)
                    .orElse(null);

            String subscriptionId = subscription != null ? subscription.getId() : null;
            int sessionCountThisMonth = 0;
            boolean isOverLimit = false;

            // Count sessions this month
            long currentCount = attendanceRepository.countByStudentIdAndSessionMonthAndSessionYearAndStatus(
                    studentAttendance.getStudentId(), month, year, AttendanceStatus.PRESENT);

            if (studentAttendance.getIsPresent()) {
                sessionCountThisMonth = (int) currentCount + 1;
                presentCount++;

                // Update subscription count
                if (subscription != null) {
                    subscription.incrementAttendedSessions();
                    subscriptionRepository.save(subscription);
                    isOverLimit = subscription.isOverLimit();
                }
            } else {
                sessionCountThisMonth = (int) currentCount;
                absentCount++;
            }

            // Create or update attendance record
            LmsAttendance attendance = attendanceRepository
                    .findBySessionIdAndStudentId(request.getSessionId(), studentAttendance.getStudentId())
                    .orElse(new LmsAttendance());

            attendance.setSessionId(request.getSessionId());
            attendance.setStudentId(studentAttendance.getStudentId());
            attendance.setStudentName(student.getFirstName() + " " + student.getLastName());
            attendance.setStudentEmail(student.getEmail());
            attendance.setSubscriptionId(subscriptionId);
            attendance.setSessionDate(session.getSessionDate());
            attendance.setSessionMonth(month);
            attendance.setSessionYear(year);
            attendance.setSessionCountThisMonth(sessionCountThisMonth);
            attendance.setIsOverLimit(isOverLimit);
            attendance.setStatus(studentAttendance.getIsPresent() ? AttendanceStatus.PRESENT : AttendanceStatus.ABSENT);
            attendance.setMarkedAt(Instant.now());
            attendance.setRemarks(studentAttendance.getRemarks());

            attendanceRepository.save(attendance);

            // Create embedded record for session
            LmsClassSession.SessionAttendanceRecord embeddedRecord = LmsClassSession.SessionAttendanceRecord.builder()
                    .studentId(studentAttendance.getStudentId())
                    .studentName(student.getFirstName() + " " + student.getLastName())
                    .studentEmail(student.getEmail())
                    .subscriptionId(subscriptionId)
                    .isPresent(studentAttendance.getIsPresent())
                    .sessionCountThisMonth(sessionCountThisMonth)
                    .isOverLimit(isOverLimit)
                    .remarks(studentAttendance.getRemarks())
                    .markedAt(Instant.now())
                    .build();

            embeddedRecords.add(embeddedRecord);
        }

        // Update session with attendance summary
        session.updateAttendanceSummary(presentCount, absentCount, presentCount + absentCount);
        session.setAttendanceRecords(embeddedRecords);
        sessionRepository.save(session);

        return sessionMapper.toResponse(session);
    }

    @Override
    public List<LmsAttendanceResponseDto> getBySessionId(String sessionId) {
        return attendanceMapper.toResponseList(attendanceRepository.findBySessionId(sessionId));
    }

    @Override
    public Page<LmsAttendanceResponseDto> getByStudentId(String studentId, Pageable pageable) {
        return attendanceRepository.findByStudentId(studentId, pageable)
                .map(attendanceMapper::toResponse);
    }

    @Override
    public Page<LmsAttendanceResponseDto> getMyAttendance(Pageable pageable) {
        String currentUserId = getCurrentUserId();
        return getByStudentId(currentUserId, pageable);
    }

    @Override
    public List<LmsAttendanceResponseDto> getOverLimitStudents(Integer month, Integer year) {
        return attendanceRepository.findBySessionMonthAndSessionYearAndIsOverLimitTrue(month, year)
                .stream()
                .map(attendanceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LmsAttendanceResponseDto getStudentMonthlyAttendance(String studentId, Integer month, Integer year) {
        List<LmsAttendance> records = attendanceRepository
                .findByStudentIdAndSessionMonthAndSessionYear(studentId, month, year);

        if (records.isEmpty()) {
            return null;
        }

        // Return the most recent record as summary
        return attendanceMapper.toResponse(records.get(records.size() - 1));
    }

    private String getCurrentUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
