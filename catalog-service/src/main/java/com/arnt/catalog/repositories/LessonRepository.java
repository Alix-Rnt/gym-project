package com.arnt.catalog.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.arnt.catalog.entities.Lesson;

@Repository
public class LessonRepository {

    private final List<Lesson> lessons = new ArrayList<>();

    /**
     * Adds a Lesson to the repository.
     * 
     * @param lesson the said Lesson to be added
     * @return the added Lesson
     */
    public Lesson save(Lesson lesson) {
        lessons.add(lesson);
        return lesson;
    }

    /**
     * Get the Lesson list.
     * 
     * @return the whole repository as a list
     */
    public List<Lesson> findAll() {
        return lessons;
    }

    /**
     * Return the first Lesson with a specific id.
     * 
     * @param id the Lesson id
     * @return the found Lesson or null if not found
     */
    public Optional<Lesson> findById(UUID id) {
        return lessons.stream()
                       .filter(e -> e.getId().equals(id))
                       .findFirst();
    }

    /**
     * Remove the first Lesson with a specific id.
     * 
     * @param id the Lesson id
     */
    public void delete(UUID id) {
        lessons.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        lessons.clear();
    }
}