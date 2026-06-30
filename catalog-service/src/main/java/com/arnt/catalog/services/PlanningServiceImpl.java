package com.arnt.catalog.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.arnt.catalog.dto.PlanningDTO;
import com.arnt.catalog.entities.Planning;
import com.arnt.catalog.exceptions.PlanningNotFoundException;
import com.arnt.catalog.repositories.PlanningRepository;
import com.arnt.catalog.status.PlanningStatus;

@Service
public class PlanningServiceImpl implements PlanningService {
    private final PlanningRepository planningRepository;
    private final LessonService lessonService;

    public PlanningServiceImpl(
            PlanningRepository planningRepository,
            LessonService lessonService) {
        this.planningRepository = planningRepository;
        this.lessonService = lessonService;
    }

    @Override
    public List<Planning> getAll() {
        return planningRepository.findAll();
    }

    @Override
    public Planning get(UUID id) {
        return planningRepository
                .findById(id)
                .orElseThrow(() -> new PlanningNotFoundException(id));
    }

    @Override
    public Planning save(PlanningDTO dto) throws IOException, InterruptedException {
        lessonService.get(dto.getLessonID());
        
        Planning planning = dto.toEntity();
        if (planning.getId() == null) {
            planning.setId(UUID.randomUUID());
        }

        return planningRepository.save(planning);
    }

    @Override
    public Planning update(UUID id, PlanningDTO dto) throws IOException, InterruptedException {
        Planning planning = this.get(id);
        lessonService.get(dto.getLessonID());

        planning.setDuration(dto.getDuration());
        planning.setDateTime(dto.getDateTime());
        planning.setStatus(dto.getStatus());
        planning.setLessonID(dto.getLessonID());

        return planningRepository.save(planning);
    }

    @Override
    public void delete(UUID id) {
        this.get(id);
        planningRepository.delete(id);
    }

    @Override
    public Planning updateStatus(UUID id, PlanningStatus status) {
        Planning planning = this.get(id);

        planning.setStatus(status);

        return planningRepository.save(planning);
    }

    @Override
    public List<Planning> getByPeriod(LocalDate start, LocalDate end) {
        return planningRepository.findAll().stream()
                .filter(e -> {
                    LocalDate date = e.getDateTime().toLocalDate();
                    return (date.isAfter(start) || date.isEqual(start)
                            && date.isBefore(end) || date.isEqual(end));
                })
                .collect(Collectors.toList());
    }

}
