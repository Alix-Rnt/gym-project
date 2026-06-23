package com.arnt.catalog.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arnt.catalog.dto.LessonDTO;
import com.arnt.catalog.entities.Lesson;
import com.arnt.catalog.exceptions.LessonNotFoundException;
import com.arnt.catalog.repositories.LessonRepository;

@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson get(UUID id) {
        return lessonRepository
                .findById(id)
                .orElseThrow(() -> new LessonNotFoundException(id));
    }

    @Override
    public Lesson save(LessonDTO dto) throws IOException, InterruptedException {
        // Validate Coach

        Lesson lesson = dto.toEntity();

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson update(UUID id, LessonDTO dto) throws IOException, InterruptedException {
        Lesson lesson = this.get(id);
        // Validate Coach

        lesson.setName(dto.getName());
        lesson.setDescription(dto.getDescription());
        lesson.setDuration(dto.getDuration());
        lesson.setDuration(dto.getDuration());
        lesson.setCoachID(dto.getCoachID());

        return lessonRepository.save(lesson);
    }

    @Override
    public void delete(UUID id) {
        this.get(id);
        lessonRepository.delete(id);
    }

}
