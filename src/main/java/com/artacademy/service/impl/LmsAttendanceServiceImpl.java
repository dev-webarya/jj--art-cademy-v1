package com.artacademy.service.impl;

import com.artacademy.dto.request.LmsAttendanceRequestDto;
import com.artacademy.dto.response.EligibleStudentDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LmsAttendanceServiceImpl implements LmsAttendanceService {

    private final LmsAttendanceRepository attendanceRepository;
    private final LmsClassSessionRepository sessionRepository;
    private final LmsStudentSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final LmsAttendanceMapper attendanceMapper;
    private final LmsClassSessionMapper sessionMapper;

    @Override
    public List<EligibleStudentDto> getEligibleStudentsForAttendance() {
        // Get current month/year
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // Get all ACTIVE subscriptions for current month
        List<LmsStudentSubscription> activeSubscriptions = subscriptionRepository
                .findBySubscriptionMonthAndSubscriptionYear(currentMonth, currentYear)
                .stream()
                .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE)
                .collect(Collectors.toList());

        log.info("Found {} active subscriptions for {}/{}", activeSubscriptions.size(), currentMonth, currentYear);

        return activeSubscriptions.stream()
                .map(sub -> EligibleStudentDto.builder()
                        .studentId(sub.getStudentId())
                        .enrollmentId(sub.getEnrollmentId())
                        .subscriptionId(sub.getId())
                        .rollNo(sub.getRollNo())
                        .studentName(sub.getStudentName())
                        .studentEmail(sub.getStudentEmail())
                        .studentPhone(sub.getStudentPhone())
                        .attendedSessions(sub.getAttendedSessions())
                        .allowedSessions(sub.getAllowedSessions())
                        .remainingSessions(sub.getRemainingSessionCount())
                        .isOverLimit(sub.isOverLimit())
                        .build())
                .collect(Collectors.toList());
    }

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

            // Find active subscription for this month (avoid non-unique result error)
            LmsStudentSubscription subscription = subscriptionRepository
                    .findByStudentIdAndSubscriptionMonthAndSubscriptionYear(
                            studentAttendance.getStudentId(), month, year)
                    .stream()
                    .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE)
                    .findFirst()
                    .orElse(null);

            String subscriptionId = subscription != null ? subscription.getId() : null;
            String rollNo = subscription != null ? subscription.getRollNo() : null;
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
            attendance.setRollNo(rollNo);
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
