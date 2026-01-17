package com.artacademy.lms.enums;

/**
 * Status for student attendance in a session
 */
public enum AttendanceStatus {
    PRESENT, // Student attended
    ABSENT, // Student did not attend
    LATE, // Student arrived late
    EXCUSED // Absent with valid reason
}
