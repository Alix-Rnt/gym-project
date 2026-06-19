package com.arnt.booking.exceptions;

import java.util.UUID;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(UUID id) {
        super("Member with id " + id + " not found.");
    }
}
