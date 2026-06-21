package com.arnt.booking.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.arnt.booking.entities.Subscription;
import com.arnt.booking.enums.SubscriptionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subscription Data Transfer Object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {
    private float price;
    private SubscriptionStatus status;
    private Integer capacity;
    private LocalDate endDate;
    private List<UUID> planningsID;
    private List<UUID> membersID;

    /**
     * Convert DTO to Subscription.
     * 
     * @return Subscription entity
     */
    public Subscription toEntity() {
        Subscription subscription = new Subscription();
        subscription.setPrice(this.price);
        subscription.setStatus(this.status);
        subscription.setCapacity(this.capacity);
        subscription.setEndDate(this.endDate);
        subscription.setPlanningsID(this.planningsID);
        subscription.setMembersID(this.membersID);
        return subscription;
    }
}
