package com.arnt.booking.entities;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Waitlist holds a list of members waiting for a specific subscription.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Waitlist {
    /** Random unique identifier. */
    private UUID id;

    /** Reference IDs to the held members with a corresponding entry timestamp. */
    private Map<UUID, Timestamp> membersTimestamp;

    /** Reference ID to the corresponding subscription. */
    private UUID subscriptionID;
}
