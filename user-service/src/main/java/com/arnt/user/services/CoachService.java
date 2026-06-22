package com.arnt.user.services;

import java.util.Collection;
import java.util.UUID;
import com.arnt.user.dto.CoachDTO;
import com.arnt.user.entities.Coach;

public interface CoachService {
    // CRUD de base du profil
    Collection<Coach> getAll();
    Coach get(UUID id);
    Coach save(CoachDTO dto);
    Coach update(UUID id, CoachDTO dto);
    void delete(UUID id);

    // --- Gestion des Références de Lessons (Catalog) ---
    Collection<UUID> getCoachLessons(UUID coachId);
    void addLesson(UUID coachId, UUID lessonId);
    void removeLesson(UUID coachId, UUID lessonId);

    // --- Gestion des Références de Subscriptions (Booking) ---
    Collection<UUID> getCoachSubscriptions(UUID coachId);
    void addSubscription(UUID coachId, UUID subscriptionId);
    void removeSubscription(UUID coachId, UUID subscriptionId);
}