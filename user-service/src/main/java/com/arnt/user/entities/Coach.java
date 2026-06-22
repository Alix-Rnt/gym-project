package com.arnt.user.entities;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Waitlist holds a list of members waiting for a specific subscription.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coach {
    /** Random unique identifier. */
    private UUID id;

    private String surname;
    
    private String name;
    
    private String email;

    private List<UUID> lessonList;
    
    private List<UUID> subscriptionList;

    /** Reference ID to the corresponding subscription. */
    private UUID userID;
}
