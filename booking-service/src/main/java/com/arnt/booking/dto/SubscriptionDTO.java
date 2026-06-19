package com.arnt.booking.dto;

import java.util.List;
import java.util.UUID;

import com.arnt.booking.entities.Subscription;
import com.arnt.booking.status.SubscriptionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subscription Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {
    private float price;
    private SubscriptionStatus status;
    private List<UUID> planningsID;

    /**
     * Convert DTO to Subscription
     * 
     * @return Subscription entity
     */

    public Subscription toEntity() {
        Subscription subscription = new Subscription();
        subscription.setPrice(this.price);
        subscription.setStatus(this.status);
        subscription.setPlanningsID(this.planningsID);
        return subscription;
    }
}
