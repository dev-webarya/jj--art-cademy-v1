package com.artacademy.lms.enums;

/**
 * Status for class sessions/tutorials
 */
public enum SessionStatus {
    SCHEDULED, // Session is planned
    IN_PROGRESS, // Session is currently happening
    COMPLETED, // Session finished
    CANCELLED, // Session was cancelled
    RESCHEDULED // Session moved to different time
}
