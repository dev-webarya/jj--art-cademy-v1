package com.artacademy.enums;

public enum ClassSchedule {
    WEEKDAY_MORNING("Weekday Morning (9AM-12PM)"),
    WEEKDAY_EVENING("Weekday Evening (4PM-7PM)"),
    WEEKEND_MORNING("Weekend Morning (9AM-12PM)"),
    WEEKEND_AFTERNOON("Weekend Afternoon (2PM-5PM)");

    private final String displayName;

    ClassSchedule(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
