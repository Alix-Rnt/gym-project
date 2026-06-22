package com.arnt.notification.exceptions;

import java.util.UUID;

public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException(String message) {
        super(message);
    }

    public TemplateNotFoundException(UUID id) {
        super("Template with id " + id + " not found.");
    }

    public static TemplateNotFoundException fromName(String name) {
        return new TemplateNotFoundException("Template with name " + name + " not found.");
    }

}
