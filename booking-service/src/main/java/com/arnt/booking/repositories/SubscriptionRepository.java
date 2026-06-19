package com.arnt.booking.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.arnt.booking.entities.Subscription;

/**
 * SubscriptionRepository holds all the subscriptions.
 */
@Repository
public class SubscriptionRepository {
    /** Subscription list. */
    private final List<Subscription> subscriptions = new ArrayList<>();

    /**
     * Adds a Subscription to the repository.
     * 
     * @param subscription the said Subscription to be added
     * @return the added Subscription
     */
    public Subscription save(Subscription subscription) {
        subscriptions.add(subscription);
        return subscription;
    }

    /**
     * Get the Subscription list.
     * 
     * @return the whole repository as a list
     */
    public List<Subscription> findAll() {
        return subscriptions;
    }

    /**
     * Return the first Subscription with a specific id.
     * 
     * @param id the Subscription id
     * @return the found Subscription or null if not found
     */
    public Optional<Subscription> findById(UUID id) {
        return subscriptions.stream()
                            .filter(e -> e.getId().equals(id))
                            .findFirst();
    }

    /**
     * Remove the first Subscription with a specific id.
     * 
     * @param id the Subscription id
     */
    public void delete(UUID id) {
        subscriptions.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        subscriptions.clear();
    }

}
