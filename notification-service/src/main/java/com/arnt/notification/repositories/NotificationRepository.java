package com.arnt.notification.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.arnt.notification.entities.Notification;

/**
 * NotificationRepository holds all notifications.
 */
@Repository
public class NotificationRepository {
    /** Template list. */
    private final List<Notification> notificaions = new ArrayList<>();

    /**
     * Adds a Notification to the repository.
     * 
     * @param notificaion the said Notification to be added
     * @return the added Notification
     */
    public Notification save(Notification notificaion) {
        notificaions.add(notificaion);
        return notificaion;
    }

    /**
     * Get the Notification list.
     * 
     * @return the whole repository as a list
     */
    public List<Notification> findAll() {
        return notificaions;
    }

    /**
     * Return the first Notification with a specific id.
     * 
     * @param id the Notification id
     * @return the found Notification or null if not found
     */
    public Optional<Notification> findById(UUID id) {
        return notificaions.stream()
                        .filter(e -> e.getId().equals(id))
                        .findFirst();
    }

    /**
     * Remove the first Notification with a specific id.
     * 
     * @param id the Notification id
     */
    public void delete(UUID id) {
        notificaions.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        notificaions.clear();
    }

}
