package com.arnt.user.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import com.arnt.user.entities.Coach;

/**
 * CoachRepository holds all the coaches.
 */
@Repository
public class CoachRepository {
    /** Coach list. */
    private final List<Coach> coaches = new ArrayList<>();

    /**
     * Adds a Coach to the repository.
     * * @param coach the said Coach to be added
     * @return the added Coach
     */
    public Coach save(Coach coach) {
        coaches.add(coach);
        return coach;
    }

    /**
     * Get the Coach list.
     * * @return the whole repository as a list
     */
    public List<Coach> findAll() {
        return coaches;
    }

    /**
     * Return the first Coach with a specific id.
     * * @param id the Coach id
     * @return the found Coach or empty Optional if not found
     */
    public Optional<Coach> findById(UUID id) {
        return coaches.stream()
                      .filter(e -> e.getId().equals(id))
                      .findFirst();
    }

    /**
     * Return the first Coach linked to a specific userID.
     * * @param userID the User id reference
     * @return the found Coach or empty Optional if not found
     */
    public Optional<Coach> findByUserID(UUID userID) {
        return coaches.stream()
                      .filter(e -> e.getUserID().equals(userID))
                      .findFirst();
    }

    /**
     * Remove the first Coach with a specific id.
     * * @param id the Coach id
     */
    public void delete(UUID id) {
        coaches.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        coaches.clear();
    }
}