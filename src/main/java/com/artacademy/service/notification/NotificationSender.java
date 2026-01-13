package com.artacademy.service.notification;

import com.artacademy.service.notification.model.NotificationMessage;

/**
 * Interface to decouple the external notification library from business logic.
 */
public interface NotificationSender {
    void send(String email, String phoneNumber, NotificationMessage message);
}