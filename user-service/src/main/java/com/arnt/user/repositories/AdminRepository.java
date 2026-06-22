package com.arnt.user.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import com.arnt.user.entities.Admin;

/**
 * AdminRepository holds all the admins.
 */
@Repository
public class AdminRepository {
    /** Admin list. */
    private final List<Admin> admins = new ArrayList<>();

    /**
     * Adds an Admin to the repository.
     * * @param admin the said Admin to be added
     * @return the added Admin
     */
    public Admin save(Admin admin) {
        admins.add(admin);
        return admin;
    }

    /**
     * Get the Admin list.
     * * @return the whole repository as a list
     */
    public List<Admin> findAll() {
        return admins;
    }

    /**
     * Return the first Admin with a specific id.
     * * @param id the Admin id
     * @return the found Admin or empty Optional if not found
     */
    public Optional<Admin> findById(UUID id) {
        return admins.stream()
                     .filter(e -> e.getId().equals(id))
                     .findFirst();
    }

    /**
     * Return the first Admin linked to a specific userID.
     * * @param userID the User id reference
     * @return the found Admin or empty Optional if not found
     */
    public Optional<Admin> findByUserID(UUID userID) {
        return admins.stream()
                     .filter(e -> e.getUserID().equals(userID))
                     .findFirst();
    }

    /**
     * Remove the first Admin with a specific id.
     * * @param id the Admin id
     */
    public void delete(UUID id) {
        admins.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        admins.clear();
    }
}