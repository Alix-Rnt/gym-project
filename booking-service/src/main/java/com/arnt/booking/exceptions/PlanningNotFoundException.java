package com.arnt.booking.exceptions;

import java.util.UUID;

public class PlanningNotFoundException extends RuntimeException {
    public PlanningNotFoundException(String message) {
        super(message);
    }

    public PlanningNotFoundException(UUID id) {
        super("Planning with id " + id + " not found.");
    }
}
