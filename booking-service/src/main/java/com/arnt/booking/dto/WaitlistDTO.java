package com.arnt.booking.dto;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import com.arnt.booking.entities.Waitlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Waitlist Data Transfer Object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitlistDTO {
    private Map<UUID, Timestamp> membersTimestamp;
    private UUID subscriptionID;

    /**
     * Convert DTO to Waitlist.
     * 
     * @return Waitlist entity
     */
    public Waitlist toEntity() {
        Waitlist waitlist = new Waitlist();
        waitlist.setMembersTimestamp(this.membersTimestamp);
        waitlist.setSubscriptionID(this.subscriptionID);
        return waitlist;
    }
}
