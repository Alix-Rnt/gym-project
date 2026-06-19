package com.arnt.booking.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.arnt.booking.entities.Waitlist;

/**
 * WaitlistRepository holds all the waitlists.
 */
@Repository
public class WaitlistRepository {
    /** Waitlist list. */
    private final List<Waitlist> waitlists = new ArrayList<>();

    /**
     * Adds a Waitlist to the repository.
     * 
     * @param waitlist the said waitlist to be added
     * @return the added waitlist
     */
    public Waitlist save(Waitlist waitlist) {
        waitlist.setId(UUID.randomUUID());
        waitlists.add(waitlist);
        return waitlist;
    }

    /**
     * Get the Waitlist list.
     * 
     * @return the whole repository as a list
     */
    public List<Waitlist> findAll() {
        return waitlists;
    }

    /**
     * Return the first Waitlist with a specific id.
     * 
     * @param id the Waitlist id
     * @return the found Waitlist or null if not found
     */
    public Optional<Waitlist> findById(UUID id) {
        return waitlists.stream()
                        .filter(e -> e.getId().equals(id))
                        .findFirst();
    }

    /**
     * Remove the first Waitlist with a specific id.
     * 
     * @param id the Waitlist id
     */
    public void delete(UUID id) {
        waitlists.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        waitlists.clear();
    }

    /**
     * Return the first Waitlist with a specific Subscription id.
     * 
     * @param id the Subscription id
     * @return the found Waitlist or null if not found
     */
    public Optional<Waitlist> findBySubscriptionId(UUID id) {
        return waitlists.stream()
                        .filter(e -> e.getSubscriptionID().equals(id))
                        .findFirst();
    }

}
