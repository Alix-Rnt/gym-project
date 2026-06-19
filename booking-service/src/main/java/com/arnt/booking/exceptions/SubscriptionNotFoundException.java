package com.arnt.booking.exceptions;

import java.util.UUID;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(String message) {
        super(message);
    }

    public SubscriptionNotFoundException(UUID id) {
        super("Subscription with id " + id + " not found.");
    }
}
