package com.arnt.notification.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.arnt.notification.types.NotificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notification is used to send information to Members via some media.
 */
@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    /** Random unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Notification type. */
    private NotificationType type;

    /** Notification subject. */
    private String subject;

    /** Notification content. */
    private String content;

    /** Notification date and time when sent. */
    private LocalDateTime sendTime;

    /** List of Member id to be sent to. */
    private List<UUID> membersID;

    /** Template id the Notification takes reference to. */
    private UUID templateID;
}
