package com.artacademy.service;

import com.artacademy.dto.attendance.*;
import com.artacademy.entity.*;
import com.artacademy.enums.AttendanceStatus;
import com.artacademy.enums.EnrollmentStatus;
import com.artacademy.enums.SessionStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final ClassEnrollmentRepository enrollmentRepository;
    private final ArtClassesRepository classesRepository;
    private final UserRepository userRepository;

    // ==================== SESSION MANAGEMENT ====================

    public AttendanceSessionDTO createSession(CreateSessionRequest request) {
        // Validate class exists
        ArtClasses artClass = classesRepository.findById(request.getClassId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Art class not found with id: " + request.getClassId()));

        // Validate teacher exists
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Teacher not found with id: " + request.getTeacherId()));

        // Generate session code
        String sessionCode = generateSessionCode(artClass.getName(), request.getSessionDate(), request.getStartTime());

        // Get enrolled students count
        long enrolledCount = enrollmentRepository.countActiveEnrollmentsByClassId(request.getClassId());

        AttendanceSession session = AttendanceSession.builder()
                .sessionCode(sessionCode)
                .artClass(AttendanceSession.ArtClassRef.builder()
                        .classId(artClass.getId())
                        .name(artClass.getName())
                        .proficiency(artClass.getProficiency())
                        .build())
                .teacher(AttendanceSession.TeacherRef.builder()
                        .userId(teacher.getId())
                        .firstName(teacher.getFirstName())
                        .lastName(teacher.getLastName())
                        .email(teacher.getEmail())
                        .build())
                .sessionDate(request.getSessionDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(SessionStatus.SCHEDULED)
                .totalEnrolled((int) enrolledCount)
                .totalPresent(0)
                .totalAbsent(0)
                .remarks(request.getRemarks())
                .build();

        session = sessionRepository.save(session);
        log.info("Created attendance session: {}", session.getSessionCode());

        return mapToSessionDTO(session);
    }

    public AttendanceSessionDTO getSessionById(String sessionId) {
        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
        return mapToSessionDTO(session);
    }

    public List<AttendanceSessionDTO> getSessionsByClassId(String classId) {
        return sessionRepository.findByClassId(classId).stream()
                .map(this::mapToSessionDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceSessionDTO> getSessionsByDate(LocalDate date) {
        return sessionRepository.findBySessionDate(date).stream()
                .map(this::mapToSessionDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceSessionDTO> getSessionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return sessionRepository.findBySessionDateBetween(startDate, endDate).stream()
                .map(this::mapToSessionDTO)
                .collect(Collectors.toList());
    }

    public AttendanceSessionDTO updateSessionStatus(String sessionId, SessionStatus status) {
        AttendanceSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));

        session.setStatus(status);
        session = sessionRepository.save(session);

        return mapToSessionDTO(session);
    }

    // ==================== ATTENDANCE MARKING ====================

    public AttendanceRecordDTO markAttendance(MarkAttendanceRequest request, User markedBy) {
        AttendanceSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Session not found with id: " + request.getSessionId()));

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        // Check if attendance already exists, update if so
        AttendanceRecord record = recordRepository
                .findBySessionIdAndStudentId(request.getSessionId(), request.getStudentId())
                .orElse(AttendanceRecord.builder()
                        .session(AttendanceRecord.SessionRef.builder()
                                .sessionId(session.getId())
                                .sessionCode(session.getSessionCode())
                                .sessionDate(session.getSessionDate())
                                .build())
                        .artClass(AttendanceRecord.ArtClassRef.builder()
                                .classId(session.getArtClass().getClassId())
                                .name(session.getArtClass().getName())
                                .build())
                        .student(AttendanceRecord.StudentRef.builder()
                                .userId(student.getId())
                                .firstName(student.getFirstName())
                                .lastName(student.getLastName())
                                .email(student.getEmail())
                                .phoneNumber(student.getPhoneNumber())
                                .build())
                        .build());

        record.setStatus(request.getStatus());
        record.setCheckInTime(request.getCheckInTime() != null ? request.getCheckInTime()
                : (request.getStatus() == AttendanceStatus.PRESENT || request.getStatus() == AttendanceStatus.LATE
                        ? Instant.now()
                        : null));
        record.setCheckOutTime(request.getCheckOutTime());
        record.setRemarks(request.getRemarks());
        record.setMarkedBy(AttendanceRecord.MarkedByRef.builder()
                .userId(markedBy.getId())
                .firstName(markedBy.getFirstName())
                .lastName(markedBy.getLastName())
                .role(markedBy.getRoles().stream().findFirst().map(User.RoleRef::getName).orElse("UNKNOWN"))
                .build());

        record = recordRepository.save(record);

        // Update session counts
        updateSessionCounts(session.getId());

        log.info("Marked attendance for student {} in session {}: {}",
                student.getEmail(), session.getSessionCode(), request.getStatus());

        return mapToRecordDTO(record);
    }

    public List<AttendanceRecordDTO> bulkMarkAttendance(BulkMarkAttendanceRequest request, User markedBy) {
        List<AttendanceRecordDTO> results = new ArrayList<>();

        for (BulkMarkAttendanceRequest.StudentAttendance sa : request.getAttendances()) {
            MarkAttendanceRequest markRequest = MarkAttendanceRequest.builder()
                    .sessionId(request.getSessionId())
                    .studentId(sa.getStudentId())
                    .status(sa.getStatus())
                    .remarks(sa.getRemarks())
                    .build();

            results.add(markAttendance(markRequest, markedBy));
        }

        return results;
    }

    // ==================== ATTENDANCE QUERIES ====================

    public List<AttendanceRecordDTO> getAttendanceBySession(String sessionId) {
        return recordRepository.findBySessionId(sessionId).stream()
                .map(this::mapToRecordDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceRecordDTO> getStudentAttendanceHistory(String studentId) {
        return recordRepository.findByStudentId(studentId).stream()
                .map(this::mapToRecordDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceRecordDTO> getStudentAttendanceByDateRange(String studentId, LocalDate startDate,
            LocalDate endDate) {
        return recordRepository.findByStudentIdAndDateRange(studentId, startDate, endDate).stream()
                .map(this::mapToRecordDTO)
                .collect(Collectors.toList());
    }

    // ==================== REPORTS ====================

    public AttendanceReportDTO getStudentAttendanceReport(String studentId, String classId) {
        List<AttendanceRecord> records = recordRepository.findByStudentId(studentId).stream()
                .filter(r -> classId == null || r.getArtClass().getClassId().equals(classId))
                .collect(Collectors.toList());

        if (records.isEmpty()) {
            return AttendanceReportDTO.builder()
                    .studentId(studentId)
                    .totalSessions(0)
                    .presentCount(0)
                    .absentCount(0)
                    .lateCount(0)
                    .excusedCount(0)
                    .attendancePercentage(0.0)
                    .build();
        }

        User student = userRepository.findById(studentId).orElse(null);

        int total = records.size();
        int present = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.PRESENT).count();
        int absent = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.ABSENT).count();
        int late = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.LATE).count();
        int excused = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.EXCUSED).count();

        return AttendanceReportDTO.builder()
                .classId(classId)
                .className(records.get(0).getArtClass().getName())
                .studentId(studentId)
                .studentName(student != null ? student.getFirstName() + " " + student.getLastName() : "Unknown")
                .totalSessions(total)
                .presentCount(present)
                .absentCount(absent)
                .lateCount(late)
                .excusedCount(excused)
                .attendancePercentage(total > 0 ? (double) (present + late) / total * 100 : 0.0)
                .build();
    }

    public List<AttendanceReportDTO> getClassAttendanceReport(String classId) {
        List<ClassEnrollment> enrollments = enrollmentRepository.findActiveEnrollmentsByClassId(classId);

        return enrollments.stream()
                .map(e -> getStudentAttendanceReport(e.getStudent().getUserId(), classId))
                .collect(Collectors.toList());
    }

    // ==================== ENROLLMENT MANAGEMENT ====================

    public ClassEnrollmentDTO enrollStudent(EnrollStudentRequest request) {
        // Check if already enrolled
        if (enrollmentRepository.findByStudentIdAndClassId(request.getStudentId(), request.getClassId()).isPresent()) {
            throw new IllegalStateException("Student is already enrolled in this class");
        }

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        ArtClasses artClass = classesRepository.findById(request.getClassId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Art class not found with id: " + request.getClassId()));

        ClassEnrollment enrollment = ClassEnrollment.builder()
                .student(ClassEnrollment.StudentRef.builder()
                        .userId(student.getId())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .email(student.getEmail())
                        .build())
                .artClass(ClassEnrollment.ArtClassRef.builder()
                        .classId(artClass.getId())
                        .name(artClass.getName())
                        .proficiency(artClass.getProficiency())
                        .build())
                .enrollmentDate(LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .paymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : "PENDING")
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        enrollment = enrollmentRepository.save(enrollment);
        log.info("Enrolled student {} in class {}", student.getEmail(), artClass.getName());

        return mapToEnrollmentDTO(enrollment);
    }

    public List<ClassEnrollmentDTO> getEnrollmentsByClassId(String classId) {
        return enrollmentRepository.findByClassId(classId).stream()
                .map(this::mapToEnrollmentDTO)
                .collect(Collectors.toList());
    }

    public List<ClassEnrollmentDTO> getEnrollmentsByStudentId(String studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::mapToEnrollmentDTO)
                .collect(Collectors.toList());
    }

    // ==================== HELPER METHODS ====================

    private String generateSessionCode(String className, LocalDate date, String startTime) {
        String classCode = className.replaceAll("[^A-Za-z0-9]", "").toUpperCase().substring(0,
                Math.min(6, className.length()));
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timeSlot = startTime.contains("AM") ? "AM" : "PM";
        return classCode + "-" + dateStr + "-" + timeSlot;
    }

    private void updateSessionCounts(String sessionId) {
        List<AttendanceRecord> records = recordRepository.findBySessionId(sessionId);

        int present = (int) records.stream()
                .filter(r -> r.getStatus() == AttendanceStatus.PRESENT || r.getStatus() == AttendanceStatus.LATE)
                .count();
        int absent = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.ABSENT).count();

        sessionRepository.findById(sessionId).ifPresent(session -> {
            session.setTotalPresent(present);
            session.setTotalAbsent(absent);
            sessionRepository.save(session);
        });
    }

    private AttendanceSessionDTO mapToSessionDTO(AttendanceSession session) {
        return AttendanceSessionDTO.builder()
                .id(session.getId())
                .sessionCode(session.getSessionCode())
                .classId(session.getArtClass().getClassId())
                .className(session.getArtClass().getName())
                .proficiency(session.getArtClass().getProficiency())
                .teacherId(session.getTeacher().getUserId())
                .teacherName(session.getTeacher().getFirstName() + " " + session.getTeacher().getLastName())
                .teacherEmail(session.getTeacher().getEmail())
                .sessionDate(session.getSessionDate())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .status(session.getStatus())
                .totalEnrolled(session.getTotalEnrolled())
                .totalPresent(session.getTotalPresent())
                .totalAbsent(session.getTotalAbsent())
                .remarks(session.getRemarks())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

    private AttendanceRecordDTO mapToRecordDTO(AttendanceRecord record) {
        return AttendanceRecordDTO.builder()
                .id(record.getId())
                .sessionId(record.getSession().getSessionId())
                .sessionCode(record.getSession().getSessionCode())
                .sessionDate(record.getSession().getSessionDate())
                .classId(record.getArtClass().getClassId())
                .className(record.getArtClass().getName())
                .studentId(record.getStudent().getUserId())
                .studentName(record.getStudent().getFirstName() + " " + record.getStudent().getLastName())
                .studentEmail(record.getStudent().getEmail())
                .studentPhone(record.getStudent().getPhoneNumber())
                .status(record.getStatus())
                .checkInTime(record.getCheckInTime())
                .checkOutTime(record.getCheckOutTime())
                .markedById(record.getMarkedBy() != null ? record.getMarkedBy().getUserId() : null)
                .markedByName(record.getMarkedBy() != null
                        ? record.getMarkedBy().getFirstName() + " " + record.getMarkedBy().getLastName()
                        : null)
                .markedByRole(record.getMarkedBy() != null ? record.getMarkedBy().getRole() : null)
                .remarks(record.getRemarks())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    private ClassEnrollmentDTO mapToEnrollmentDTO(ClassEnrollment enrollment) {
        return ClassEnrollmentDTO.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getUserId())
                .studentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName())
                .studentEmail(enrollment.getStudent().getEmail())
                .classId(enrollment.getArtClass().getClassId())
                .className(enrollment.getArtClass().getName())
                .proficiency(enrollment.getArtClass().getProficiency())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .paymentStatus(enrollment.getPaymentStatus())
                .startDate(enrollment.getStartDate())
                .endDate(enrollment.getEndDate())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }
}
