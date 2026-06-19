package com.arnt.booking.exceptions;

import java.util.UUID;

public class WaitlistNotFoundException extends RuntimeException {
    public WaitlistNotFoundException(String message) {
        super(message);
    }

    public WaitlistNotFoundException(UUID id) {
        super("Waitlist with id " + id + " not found.");
    }
}
