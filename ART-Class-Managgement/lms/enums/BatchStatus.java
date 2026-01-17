package com.artacademy.lms.enums;

/**
 * Status for batches - especially when instructor is removed
 */
public enum BatchStatus {
    ACTIVE, // Normal operation
    NEEDS_INSTRUCTOR, // Instructor was removed, needs reassignment
    COMPLETED, // Batch finished
    SUSPENDED, // Temporarily paused
    CANCELLED // Batch was cancelled
}
