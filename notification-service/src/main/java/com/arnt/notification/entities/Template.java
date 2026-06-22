package com.arnt.notification.entities;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Template holds static information used in a Notification.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    /** Random unique identifier. */
    private UUID id;

    /** Template name. */
    private String name;

    /** Template subject. */
    private String subject;

    /** Template content. */
    private String content;
}
