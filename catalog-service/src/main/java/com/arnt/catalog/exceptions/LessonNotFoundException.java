package com.arnt.catalog.exceptions;

import java.util.UUID;

public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(String message) {
        super(message);
    }

    public LessonNotFoundException(UUID id) {
        super("Lesson with id " + id + " not found.");
    }
}
