package com.arnt.catalog.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arnt.catalog.dto.LessonDTO;
import com.arnt.catalog.dto.PlanningDTO;
import com.arnt.catalog.entities.Lesson;
import com.arnt.catalog.entities.Planning;
import com.arnt.catalog.services.LessonService;
import com.arnt.catalog.services.PlanningService;
import com.arnt.catalog.status.PlanningStatus;


@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
    private final PlanningService planningService;
    private final LessonService lessonService;

    /**
     * CatalogController constructor.
     * 
     * @param planningService
     * @param lessonService
     */
    public CatalogController(
            PlanningService planningService,
            LessonService lessonService) {
        this.planningService = planningService;
        this.lessonService = lessonService;
    }


    /*
        =====================
        Lesson requests
        =====================
    */

    /**
     * List all Lessons.
     * GET /lessons
     * 
     * @return a list of every Lesson
     */
    @GetMapping("/lessons")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Lesson> getLessons() {
        return lessonService.getAll();
    }

    /**
     * Create a Lesson.
     * POST /lessons
     * 
     * @param dto the Lesson DTO body
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public Lesson createLesson(@RequestBody LessonDTO dto) throws IOException, InterruptedException {
        return lessonService.save(dto);
    }

    /**
     * Get one Lesson.
     * GET /lessons/{id}
     * 
     * @param id the Lesson id
     * @return the found Lesson
     */
    @GetMapping("/lessons/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Lesson getLesson(@PathVariable UUID id) {
        return lessonService.get(id);
    }

    /**
     * Update one Lesson.
     * PUT /lessons/{id}
     * 
     * @param dto the new information
     * @param id the Lesson id
     */
    @PutMapping("/lessons/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Lesson updateLesson(@RequestBody LessonDTO dto, @PathVariable UUID id) throws IOException, InterruptedException {
        return lessonService.update(id, dto);
    }

    /**
     * Delete one Lesson.
     * DELETE /lessons/{id}
     * 
     * @param id the Lesson id
     */
    @DeleteMapping("/lessons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(@PathVariable UUID id) {
        lessonService.delete(id);
    }

    /*
        =====================
        Planning requests
        =====================
    */

    /**
     * List all Planning.
     * GET /plannings
     * 
     * @return a list of every Planning
     */
    @GetMapping("/plannings")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Planning> getPlannings() {
        return planningService.getAll();
    }

    /**
     * Create a Planning.
     * POST /plannings
     * 
     * @param dto the Planning DTO body
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("/plannings")
    @ResponseStatus(HttpStatus.CREATED)
    public Planning createPlanning(@RequestBody PlanningDTO dto) throws IOException, InterruptedException {
        return planningService.save(dto);
    }

    /**
     * Get one Planning.
     * GET /plannings/{id}
     * 
     * @param id the Planning id
     * @return the found Planning
     */
    @GetMapping("/plannings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Planning getPlanning(@PathVariable UUID id) {
        return planningService.get(id);
    }

    /**
     * Update one Planning.
     * PUT /plannings/{id}
     * 
     * @param dto the new information
     * @param id the Planning id
     */
    @PutMapping("/plannings/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Planning updatePlanning(@RequestBody PlanningDTO dto, @PathVariable UUID id) throws IOException, InterruptedException {
        return planningService.update(id, dto);
    }

    /**
     * Delete one Planning.
     * DELETE /plannings/{id}
     * 
     * @param id the Planning id
     */
    @DeleteMapping("/plannings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlanning(@PathVariable UUID id) {
        planningService.delete(id);
    }

    /**
     * Update Planning status
     * PATCH /plannings/{id}/status
     * 
     * @param id the Planning id
     * @param status the next status
     * @return the updated Planning
     */
    @PatchMapping("/plannings/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public Planning updateStatus(@PathVariable UUID id, @RequestBody PlanningStatus status) {
        return planningService.updateStatus(id, status);
    }

        /**
     * Get all Plannings in a period.
     * GET /plannings?startDate={startDate}&endDate={endDate}
     * @param startDate the start date of the period
     * @param endDate the end date of the period
     * @return the Plannings corresponding to the period
     */
    @GetMapping("/plannings?startDate={startDate}&endDate={endDate}")
    @ResponseStatus(HttpStatus.OK)
    public List<Planning> getPlanningsInPeriod(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return planningService.getByPeriod(startDate, endDate);
    }
}