package com.arnt.user.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import com.arnt.user.entities.User;

/**
 * UserRepository holds all the users.
 */
@Repository
public class UserRepository {
    /** User list. */
    private final List<User> users = new ArrayList<>();

    /**
     * Adds a User to the repository.
     * * @param user the said User to be added
     * @return the added User
     */
    public User save(User user) {
        users.add(user);
        return user;
    }

    /**
     * Get the User list.
     * * @return the whole repository as a list
     */
    public List<User> findAll() {
        return users;
    }

    /**
     * Return the first User with a specific id.
     * * @param id the User id
     * @return the found User or empty Optional if not found
     */
    public Optional<User> findById(UUID id) {
        return users.stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst();
    }

    /**
     * Return the first User with a specific username.
     * Useful for login/authentication processes.
     * * @param username the User username
     * @return the found User or empty Optional if not found
     */
    public Optional<User> findByUsername(String username) {
        return users.stream()
                    .filter(e -> e.getUsername().equalsIgnoreCase(username))
                    .findFirst();
    }

    /**
     * Remove the first User with a specific id.
     * * @param id the User id
     */
    public void delete(UUID id) {
        users.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        users.clear();
    }
}