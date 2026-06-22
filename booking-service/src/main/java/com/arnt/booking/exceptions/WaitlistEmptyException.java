package com.arnt.booking.exceptions;

import java.util.UUID;

public class WaitlistEmptyException extends RuntimeException {
    public WaitlistEmptyException(String message) {
        super(message);
    }

    public WaitlistEmptyException(UUID id) {
        super("Waitlist with id " + id + " is empty.");
    }
}