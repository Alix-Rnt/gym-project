package com.arnt.booking.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.arnt.booking.dto.SubscriptionDTO;
import com.arnt.booking.entities.Subscription;
import com.arnt.booking.enums.SubscriptionStatus;

/**
 * SubscriptionService implements methods related to Subscription management.
 */
public interface SubscriptionService {
    // Base methods related to SubscriptionRepository.
    List<Subscription> getAll();
    Subscription get(UUID id);
    Subscription save(SubscriptionDTO dto) throws IOException, InterruptedException;
    Subscription update(UUID id, SubscriptionDTO dto) throws IOException, InterruptedException;
    void delete(UUID id);

    /**
     * Update Subscription status.
     * 
     * @param id the Subscription id
     * @param status the next status
     * @return the updated Subscription
     */
    Subscription updateStatus(UUID id, SubscriptionStatus status);

    /**
     * Add a Planning ID to the Subscription planningsID list.
     * 
     * @param id the Subscription id
     * @param planningId the Planning id
     * @return the updated Subscription
     */
    Subscription addPlanning(UUID id, UUID planningId) throws IOException, InterruptedException;

    /**
     * Remove a Planning ID from the Subscription planningsID list.
     * 
     * @param id the Subscription id
     * @param planningId the Planning id
     * @return the updated Subscription
     */
    Subscription removePlanning(UUID id, UUID planningId);

    /**
     * Add a Member ID from the Subscription membersID list.
     * 
     * @param id the Subscription id
     * @param planningId the Member id
     * @return the updated Subscription
     */
    Subscription addMember(UUID id, UUID memberId) throws IOException, InterruptedException;

    /**
     * Remove a Member ID from the Subscription membersID list.
     * Then add the first Member in the Waitlist in the membersID list if there is.
     * Then publish an event with that new Member id.
     * 
     * @param id the Subscription id
     * @param planningId the Member id
     * @return the updated Subscription
     */
    Subscription removeMember(UUID id, UUID memberId) throws IOException, InterruptedException;

    /**
     * Remove a Planning ID from all Subscription having it.
     * Then publish an event for every Planning removed.
     * 
     * @param planningId the planning id
     */
    void removePlanningFromAll(UUID planningId);
}
