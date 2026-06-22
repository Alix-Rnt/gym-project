package com.arnt.catalog.dto;

import java.util.UUID;
import java.time.LocalDateTime;

import com.arnt.catalog.entities.Planning;
import com.arnt.catalog.status.PlanningStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subscription Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanningDTO {
    private UUID id;
    private float Duration;
    private LocalDateTime dateTime;
    private PlanningStatus Status;
    private UUID lessonID;

    /**
     * Convert DTO to Planning
     * 
     * @return Planning entity
     */

    public Planning toEntity() {
        Planning planning = new Planning();
        planning.setId(this.id);
        planning.setDuration(this.Duration);
        planning.setDateTime(this.dateTime);
        planning.setStatus(this.Status);
        planning.setLessonID(this.lessonID);
        return planning;
    }
}
