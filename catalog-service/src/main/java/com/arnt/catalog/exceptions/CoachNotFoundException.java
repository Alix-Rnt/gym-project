package com.arnt.catalog.exceptions;

import java.util.UUID;

public class CoachNotFoundException extends RuntimeException {
    public CoachNotFoundException(String message) {
        super(message);
    }

    public CoachNotFoundException(UUID id) {
        super("Coach with id " + id + " not found.");
    }
}
