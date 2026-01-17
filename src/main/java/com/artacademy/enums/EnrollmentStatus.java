package com.artacademy.enums;

public enum EnrollmentStatus {
    PENDING, // Initial status when student enrolls
    APPROVED, // Admin confirmed enrollment
    REJECTED, // Admin rejected enrollment
    COMPLETED, // Class completed
    CANCELLED // Student cancelled enrollment
}
