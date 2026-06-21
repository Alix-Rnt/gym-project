package com.arnt.catalog.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.arnt.catalog.entities.Planning;

@Repository
public class PlanningRepository {

    private final List<Planning> plannings = new ArrayList<>();

    /**
     * Adds a Planning to the repository.
     * 
     * @param planning the said Planning to be added
     * @return the added Planning
     */
    public Planning save(Planning planning) {
        plannings.add(planning);
        return planning;
    }

    /**
     * Get the Planning list.
     * 
     * @return the whole repository as a list
     */
    public List<Planning> findAll() {
        return plannings;
    }

    /**
     * Return the first Planning with a specific id.
     * 
     * @param id the Planning id
     * @return the found Planning or null if not found
     */
    public Optional<Planning> findById(UUID id) {
        return plannings.stream()
                         .filter(e -> e.getId().equals(id))
                         .findFirst();
    }

    /**
     * Remove the first Planning with a specific id.
     * 
     * @param id the Planning id
     */
    public void delete(UUID id) {
        plannings.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        plannings.clear();
    }
}