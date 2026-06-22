package com.arnt.user.dto;

import java.util.UUID;
import java.util.List;

import com.arnt.user.entities.Coach;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoachDTO {
    private String surname;
    private String name;
    private String email;
    private List<UUID> lessonList;
    private List<UUID> subscriptionList;
    private UUID userID;

    /**
     * Convert DTO to Coach
     * 
     * @return Coach entity
     */

    public Coach toEntity() {
        Coach coach = new Coach();
        coach.setSurname(this.surname);
        coach.setName(this.name);
        coach.setEmail(this.email);
        coach.setLessonList(this.lessonList);
        coach.setSubscriptionList(this.subscriptionList);
        coach.setUserID(this.userID);
        return coach;
    }
}
