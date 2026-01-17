package com.artacademy.lms.enums;

/**
 * Status for assignments
 */
public enum AssignmentStatus {
    DRAFT, // Not yet visible to students
    PUBLISHED, // Active and visible
    CLOSED // Past due date, no more submissions
}
