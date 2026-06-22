package com.arnt.notification.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Template holds static information used in a Notification.
 */
@Entity
@Table(name = "template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    /** Random unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Template name. */
    private String name;

    /** Template subject. */
    private String subject;

    /** Template content. */
    private String content;
}
