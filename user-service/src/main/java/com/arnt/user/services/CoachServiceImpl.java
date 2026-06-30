package com.arnt.user.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.arnt.user.dto.CoachDTO;
import com.arnt.user.entities.Coach;
import com.arnt.user.repositories.CoachRepository;

@Service
public class CoachServiceImpl implements CoachService {

    private final CoachRepository coachRepository;

    public CoachServiceImpl(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @Override
    public Collection<Coach> getAll() {
        return coachRepository.findAll();
    }

    @Override
    public Coach get(UUID id) {
        return coachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coach not found with ID: " + id));
    }

    @Override
    public Coach save(CoachDTO dto) {
        Coach coach = new Coach();
        coach.setId(UUID.randomUUID());
        coach.setSurname(dto.getSurname());
        coach.setName(dto.getName());
        coach.setEmail(dto.getEmail());
        coach.setUserID(dto.getUserID());
        
        // Initialisation des listes d'UUID vides
        coach.setLessonList(new ArrayList<UUID>());
        coach.setSubscriptionList(new ArrayList<UUID>());
        
        return coachRepository.save(coach);
    }

    @Override
    public Coach update(UUID id, CoachDTO dto) {
        Coach existingCoach = get(id);
        existingCoach.setSurname(dto.getSurname());
        existingCoach.setName(dto.getName());
        existingCoach.setEmail(dto.getEmail());
        return existingCoach;
    }

    @Override
    public void delete(UUID id) {
        coachRepository.delete(id);
    }

    // =========================================================================
    // REFERENCES : LESSONS (Lien logique avec le Microservice Catalog)
    // =========================================================================

    @Override
    public Collection<UUID> getCoachLessons(UUID coachId) {
        return get(coachId).getLessonList();
    }

    @Override
    public void addLesson(UUID coachId, UUID lessonId) {
        Coach coach = get(coachId);
        if (!coach.getLessonList().contains(lessonId)) {
            coach.getLessonList().add(lessonId);
        }
    }

    @Override
    public void removeLesson(UUID coachId, UUID lessonId) {
        Coach coach = get(coachId);
        coach.getLessonList().remove(lessonId);
    }

    // =========================================================================
    // REFERENCES : SUBSCRIPTIONS (Lien logique avec le Microservice Booking)
    // =========================================================================

    @Override
    public Collection<UUID> getCoachSubscriptions(UUID coachId) {
        return get(coachId).getSubscriptionList();
    }

    @Override
    public void addSubscription(UUID coachId, UUID subscriptionId) {
        Coach coach = get(coachId);
        if (!coach.getSubscriptionList().contains(subscriptionId)) {
            coach.getSubscriptionList().add(subscriptionId);
        }
    }

    @Override
    public void removeSubscription(UUID coachId, UUID subscriptionId) {
        Coach coach = get(coachId);
        coach.getSubscriptionList().remove(subscriptionId);
    }
}