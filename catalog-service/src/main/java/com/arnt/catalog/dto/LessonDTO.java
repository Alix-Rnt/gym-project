package com.arnt.catalog.dto;

import java.util.UUID;

import com.arnt.catalog.entities.Lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subscription Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDTO {
    private String name;
    private String description;
    private float duration;
    private String type;
    private UUID coachID;

    /**
     * Convert DTO to Lesson
     * 
     * @return Lesson entity
     */

    public Lesson toEntity() {
        Lesson lesson = new Lesson();
        lesson.setName(this.name);
        lesson.setDescription(this.description);
        lesson.setDuration(this.duration);
        lesson.setType(this.type);
        lesson.setCoachID(this.coachID);
        return lesson;
    }
}
