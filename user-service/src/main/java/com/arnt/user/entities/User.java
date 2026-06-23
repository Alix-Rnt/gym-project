package com.arnt.user.entities;

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
public class User {
    /** Random unique identifier. */
    private UUID id;

    private String username;
    
    private String password;

    private String role;
    
    /** Reference ID to the corresponding subscription. */
    private UUID roleID;
}
