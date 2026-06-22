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
public class Admin {
    /** Random unique identifier. */
    private UUID id;

    /** Reference ID to the corresponding subscription. */
    private UUID userID;
}
