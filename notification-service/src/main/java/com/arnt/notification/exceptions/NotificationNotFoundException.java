package com.arnt.notification.exceptions;

import java.util.UUID;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super(message);
    }

    public NotificationNotFoundException(UUID id) {
        super("Notification with id " + id + " not found.");
    }
}
