package com.arnt.catalog.entities;

import java.util.UUID;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Lesson contains the name, type, duration and description as well as the coach that teaches the lesson.
 */
@Entity
@Table(name = "waitlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    /** Random unique identifier. */
    private UUID id;

    /** Name of the lesson. */
    private String Name;

    /** Lesson description. */
    private String Description;

    /** Lesson duration in minutes. */
    private float Duration;

    /** Lesson type. */
    private String Type;

    /** Reference ID to the coach of the lesson. */
    private UUID coachID;
}