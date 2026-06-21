package com.arnt.catalog.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.arnt.catalog.dto.LessonDTO;
import com.arnt.catalog.entities.Lesson;

/**
 * LessonService implements methods related to Lesson management.
 */
public interface LessonService {
    // Base methods related to LessonRepository.
    List<Lesson> getAll();
    Lesson get(UUID id);
    Lesson save(LessonDTO dto) throws IOException, InterruptedException;
    Lesson update(UUID id, LessonDTO dto) throws IOException, InterruptedException;
    void delete(UUID id);


}
