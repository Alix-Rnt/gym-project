package com.arnt.catalog.entities;

import java.util.UUID;
import java.time.LocalDateTime;

import com.arnt.catalog.status.PlanningStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Planning indicates when a lesson takes place, its capacity and its status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planning {
    /** Random unique identifier. */
    private UUID id;

    /** Lesson duration in minutes. */
    private float Duration;

    /** Indicates the date and time of the lesson. */
    private LocalDateTime dateTime;

    // /** Indicates how many people can attend the lesson. */
    // private int Capacity;

    /** Lesson Status. */
    private PlanningStatus Status;

    /** Reference ID to the lesson. */
    private UUID lessonID;
}
