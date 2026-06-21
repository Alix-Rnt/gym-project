package com.arnt.booking.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.arnt.booking.enums.SubscriptionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subscription holds a list of plannings with a corresponding price.
 */
@Entity
@Table(name = "subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    /** Random unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Price in dollars. */
    private float price;

    /** Status refering to its availability. */
    private SubscriptionStatus status;

    /** Maximum member capacity. Once reached, use a Waitlist. */
    private Integer capacity;

    /** The date at which the Subscription ends. Dynamic according to the plannings dates. */
    private LocalDate endDate;

    /** Reference IDs to the held plannings. */
    private List<UUID> planningsID;

    /** Reference IDs to the subscribed members. */
    private List<UUID> membersID;
}