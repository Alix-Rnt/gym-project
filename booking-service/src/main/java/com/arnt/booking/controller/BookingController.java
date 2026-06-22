package com.arnt.booking.controller;

import java.io.IOException;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arnt.booking.dto.SubscriptionDTO;
import com.arnt.booking.dto.WaitlistDTO;
import com.arnt.booking.entities.Subscription;
import com.arnt.booking.entities.Waitlist;
import com.arnt.booking.enums.SubscriptionStatus;
import com.arnt.booking.services.SubscriptionService;
import com.arnt.booking.services.WaitlistService;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final SubscriptionService subscriptionService;
    private final WaitlistService waitlistService;

    /**
     * BookingController constructor.
     * 
     * @param subscriptionService
     * @param waitlistService
     */
    public BookingController(
            SubscriptionService subscriptionService,
            WaitlistService waitlistService
    ) {
        this.subscriptionService = subscriptionService;
        this.waitlistService = waitlistService;
    }

    /*
        =====================
        Subscription requests
        =====================
    */

    /**
     * List all Subscription.
     * GET /subscriptions
     * 
     * @return a list of every Subscription
     */
    @GetMapping("/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Subscription> getSubscriptions() {
        return subscriptionService.getAll();
    }

    /**
     * Create a Subscription.
     * POST /subscriptions
     * 
     * @param dto the Subscription DTO body
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("/subscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public Subscription createSubscription(@RequestBody SubscriptionDTO dto) throws IOException, InterruptedException {
        return subscriptionService.save(dto);
    }

    /**
     * Get one Subscription.
     * GET /subscriptions/{id}
     * 
     * @param id the Subscription id
     * @return the found Subscription
     */
    @GetMapping("/subscriptions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Subscription getSubscription(@PathVariable UUID id) {
        return subscriptionService.get(id);
    }

    /**
     * Update one Subscription.
     * PUT /subscriptions/{id}
     * 
     * @param dto the new information
     * @param id the Subscription id
     */
    @PutMapping("/subscriptions/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Subscription updateSubscription(@RequestBody SubscriptionDTO dto, @PathVariable UUID id) throws IOException, InterruptedException {
        return subscriptionService.update(id, dto);
    }

    /**
     * Delete one Subscription.
     * DELETE /subscriptions/{id}
     * 
     * @param id the Subscription id
     */
    @DeleteMapping("/subscriptions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable UUID id) {
        subscriptionService.delete(id);
    }

    /**
     * Get the Waitlist of a Subscription.
     * GET /subscriptions/{id}/waitlist
     *
     * @param id the Subscription id
     * @return the Waitlist corresponding to the Subscription
     */
    @GetMapping("/subscriptions/{id}/waitlist")
    @ResponseStatus(HttpStatus.OK)
    public Waitlist getWaitlistBySubscription(@PathVariable UUID id) {
        return waitlistService.getBySubscriptionId(id);
    }
    
    /**
     * Update Subscription status
     * PATCH /subscriptions/{id}/status
     * 
     * @param id the Subscription id
     * @param status the next status
     * @return the updated Subscription
     */
    @PatchMapping("/subscriptions/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public Subscription updateStatus(@PathVariable UUID id, @RequestBody SubscriptionStatus status) {
        return subscriptionService.updateStatus(id, status);
    }

    /**
     * Add a Planning ID to the Subscription planningsID list
     * POST /subscriptions/{id}/plannings/{planningId}
     * 
     * @param id the Subscription id
     * @param planningId the planning id
     * @return the updated Subscription
     * @throws InterruptedException 
     * @throws IOException 
     */
    @PostMapping("/subscriptions/{id}/plannings/{planningId}")
    @ResponseStatus(HttpStatus.OK)
    public Subscription addPlanning(@PathVariable UUID id, @PathVariable UUID planningId) throws IOException, InterruptedException {
        return subscriptionService.addPlanning(id, planningId);
    }

    /**
     * Remove a Planning ID from the Subscription planningsID list
     * DELETE /subscriptions/{id}/plannings/{planningId}
     * 
     * @param id the Subscription id
     * @param planningId the planning id
     * @return the updated Subscription
     */
    @DeleteMapping("/subscriptions/{id}/plannings/{planningId}")
    @ResponseStatus(HttpStatus.OK)
    public Subscription removePlanning(@PathVariable UUID id, @PathVariable UUID planningId) {
        return subscriptionService.removePlanning(id, planningId);
    }

    /*
        =================
        Waitlist requests
        =================
    */

    /**
     * List all Waitlist.
     * GET /waitlists
     * 
     * @return a list of every Waitlist
     */

    @GetMapping("/waitlists")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Waitlist> getWaitlists() {
        return waitlistService.getAll();
    }

    /**
     * Create a Waitlist.
     * POST /waitlists
     * 
     * @param dto the Waitlist DTO
     */
    @PostMapping("/waitlists")
    @ResponseStatus(HttpStatus.CREATED)
    public Waitlist createWaitlist(@RequestBody WaitlistDTO dto) {
        return waitlistService.save(dto);
    }

    /**
     * Get one Waitlist.
     * GET /waitlists/{id}
     * 
     * @param id the Waitlist id
     * @return the found Waitlist
     */
    @GetMapping("/waitlists/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Waitlist getWaitlist(@PathVariable UUID id) {
        return waitlistService.get(id);
    }

    /**
     * Update one Waitlist.
     * PUT /waitlists/{id}
     * 
     * @param dto the new information
     * @param id the Waitlist id
     */
    @PutMapping("/waitlists/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Waitlist updateWaitlist(@RequestBody WaitlistDTO dto, @PathVariable UUID id) {
        return waitlistService.update(id, dto);
    }

    /**
     * Delete one Waitlist.
     * DELETE /waitlists/{id}
     * 
     * @param id the Waitlist id
     */
    @DeleteMapping("/waitlists/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWaitlist(@PathVariable UUID id) {
        waitlistService.delete(id);
    }

    /**
     * Add a Member to a Waitlist.
     * POST /waitlists/{id}/members/{memberId}
     * 
     * @param id the Waitlist id
     * @param memberId the member id
     */
    @PostMapping("/waitlists/{id}/members/{memberId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMember(@PathVariable UUID id, @PathVariable UUID memberId) throws IOException, InterruptedException {
        waitlistService.addMember(id, memberId);
    }

    /**
     * Remove a Member from a Waitlist.
     * DELETE /waitlists/{id}/members/{memberId}
     * 
     * @param id the Waitlist id
     * @param memberId the member id
     */
    @DeleteMapping("/waitlists/{id}/members/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(@PathVariable UUID id, @PathVariable UUID memberId) throws IOException, InterruptedException {
        waitlistService.removeMember(id, memberId);
    }

}
