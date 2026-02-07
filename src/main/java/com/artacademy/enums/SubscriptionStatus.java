package com.artacademy.enums;

/**
 * Status of a student's monthly subscription.
 */
public enum SubscriptionStatus {
    ACTIVE("Active subscription"),
    EXPIRED("Subscription has expired"),
    CANCELLED("Subscription cancelled by admin"),
    PAUSED("Subscription temporarily paused");

    private final String description;

    SubscriptionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
