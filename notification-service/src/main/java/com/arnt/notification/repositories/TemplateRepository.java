package com.arnt.notification.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.arnt.notification.entities.Template;

/**
 * TemplateRepository holds all templates.
 */
@Repository
public class TemplateRepository {
    /** Template list. */
    private final List<Template> templates = new ArrayList<>();

    /**
     * Adds a Template to the repository.
     * 
     * @param template the said Template to be added
     * @return the added Template
     */
    public Template save(Template template) {
        templates.add(template);
        return template;
    }

    /**
     * Get the Template list.
     * 
     * @return the whole repository as a list
     */
    public List<Template> findAll() {
        return templates;
    }

    /**
     * Return the first Template with a specific id.
     * 
     * @param id the Template id
     * @return the found Template or null if not found
     */
    public Optional<Template> findById(UUID id) {
        return templates.stream()
                        .filter(e -> e.getId().equals(id))
                        .findFirst();
    }

    /**
     * Remove the first Template with a specific id.
     * 
     * @param id the Template id
     */
    public void delete(UUID id) {
        templates.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        templates.clear();
    }

    /**
     * Return the first Template with a specific name.
     * 
     * @param name the Template name
     * @return the found Template or null if not found
     */
    public Optional<Template> findByName(String name) {
        return templates.stream()
                        .filter(e -> e.getName().equals(name))
                        .findFirst();
    }
}
