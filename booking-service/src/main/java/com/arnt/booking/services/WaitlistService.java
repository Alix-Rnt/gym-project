package com.arnt.booking.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.arnt.booking.dto.WaitlistDTO;
import com.arnt.booking.entities.Waitlist;

/**
 * WaitlistService implements methods related to Waitlist management.
 */
public interface WaitlistService {
    // Base methods related to WaitlistRepository.
    List<Waitlist> getAll();
    Waitlist get(UUID id);
    Waitlist save(WaitlistDTO dto);
    Waitlist update(UUID id, WaitlistDTO dto);
    void delete(UUID id);

    /**
     * Return the Waitlist associated to a specific Subscirption.
     * 
     * @param id the Subscription id
     * @return the Waitlist
     */
    Waitlist getBySubscriptionId(UUID id);

    /**
     * Add a Member to a Waitlist.
     * 
     * @param id the Waitlist id
     * @param memberId the member id
     */
    void addMember(UUID id, UUID memberId) throws IOException, InterruptedException;

    /**
     * Remove a Member from a Waitlist.
     * 
     * @param id the Waitlist id
     * @param memberId the member id
     */
    void removeMember(UUID id, UUID memberId);
}
