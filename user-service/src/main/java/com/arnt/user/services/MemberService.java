package com.arnt.user.services;

import java.util.Collection;
import java.util.UUID;
import com.arnt.user.dto.MemberDTO;
import com.arnt.user.entities.Member;

public interface MemberService {
    // CRUD de base du profil
    Collection<Member> getAll();
    Member get(UUID id);
    Member save(MemberDTO dto); // Utilisé également par le UserService lors du register
    Member update(UUID id, MemberDTO dto);
    void delete(UUID id);

    // --- Gestion des Références de Subscriptions (Booking) ---
    Collection<UUID> getMemberSubscriptions(UUID memberId);
    void addSubscription(UUID memberId, UUID subscriptionId);
    void removeSubscription(UUID memberId, UUID subscriptionId);

    // --- Gestion des Références de Waitlists (Booking) ---
    Collection<UUID> getMemberWaitlists(UUID memberId);
    void addWaitlist(UUID memberId, UUID waitlistId);
    void removeWaitlist(UUID memberId, UUID waitlistId);
}