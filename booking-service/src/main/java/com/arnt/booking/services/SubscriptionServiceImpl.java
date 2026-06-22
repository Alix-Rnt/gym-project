package com.arnt.booking.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.arnt.booking.dto.SubscriptionDTO;
import com.arnt.booking.entities.Subscription;
import com.arnt.booking.entities.Waitlist;
import com.arnt.booking.enums.SubscriptionStatus;
import com.arnt.booking.events.outbound.PlanningRemovedFromSubscriptionEvent;
import com.arnt.booking.events.outbound.SubscriptionExpiredEvent;
import com.arnt.booking.events.outbound.SubscriptionInvalidatedEvent;
import com.arnt.booking.events.outbound.WaitlistPromotedEvent;
import com.arnt.booking.exceptions.PlanningNotFoundException;
import com.arnt.booking.exceptions.SubscriptionNotFoundException;
import com.arnt.booking.kafka.producer.BookingEventProducer;
import com.arnt.booking.repositories.SubscriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final WaitlistService waitlistService;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final BookingEventProducer producer;

    @Value("$(catalog.service.url)")
    private String catalogServiceUrl;

    public SubscriptionServiceImpl(
            SubscriptionRepository subscriptionRepository,
            WaitlistService waitlistService,
            BookingEventProducer producer) {
        this.subscriptionRepository = subscriptionRepository;
        this.waitlistService = waitlistService;
        this.producer = producer;
    }

    @Override
    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription get(UUID id) {
        return subscriptionRepository
                .findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
    }

    @Override
    public Subscription save(SubscriptionDTO dto) throws IOException, InterruptedException {
        validatePlanningList(dto.getPlanningsID());
        
        Subscription subscription = dto.toEntity();

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription update(UUID id, SubscriptionDTO dto) throws IOException, InterruptedException {
        Subscription subscription = this.get(id);
        validatePlanningList(dto.getPlanningsID());

        subscription.setPrice(dto.getPrice());
        subscription.setStatus(dto.getStatus());
        subscription.setPlanningsID(dto.getPlanningsID());

        for (UUID planningId : dto.getPlanningsID()) {
            LocalDate planningDate = getPlanningDateTime(planningId).toLocalDate();
            if (subscription.getEndDate() == null || planningDate.isAfter(subscription.getEndDate())) {
                subscription.setEndDate(planningDate);
            }
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public void delete(UUID id) {
        this.get(id);
        subscriptionRepository.delete(id);
    }

    @Override
    public Subscription updateStatus(UUID id, SubscriptionStatus status) {
        Subscription subscription = this.get(id);

        subscription.setStatus(status);

        if (status == SubscriptionStatus.EXPIRED) {
            producer.publishSubscriptionExpired(
                new SubscriptionExpiredEvent(id, subscription.getName(), subscription.getMembersID())
            );
        }

        if (status == SubscriptionStatus.INVALID) {
            producer.publishSubscriptionInvalidated(
                new SubscriptionInvalidatedEvent(id, subscription.getName(), subscription.getMembersID())
            );
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription addPlanning(UUID id, UUID planningId) throws IOException, InterruptedException {
        Subscription subscription = this.get(id);
        validatePlanning(planningId);

        subscription.getPlanningsID().add(planningId);

        LocalDate planningDate = getPlanningDateTime(planningId).toLocalDate();
        if (subscription.getEndDate() == null || planningDate.isAfter(subscription.getEndDate())) {
            subscription.setEndDate(planningDate);
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription removePlanning(UUID id, UUID planningId) {
        Subscription subscription = this.get(id);

        subscription.getPlanningsID().remove(planningId);

        producer.publishPlanningRemovedFromSubscription(
            new PlanningRemovedFromSubscriptionEvent(
                    subscription.getId(),
                    planningId,
                    subscription.getMembersID())
        );

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription addMember(UUID id, UUID memberId) throws IOException, InterruptedException {
        Subscription subscription = this.get(id);

        if (subscription.getMembersID().size() < subscription.getCapacity()) {
            subscription.getMembersID().add(memberId);
        } else {
            waitlistService.addMemberBySubscriptionId(id, memberId);
        }
        
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription removeMember(UUID id, UUID memberId) throws IOException, InterruptedException {
        Subscription subscription = this.get(id);

        subscription.getMembersID().remove(memberId);

        Waitlist waitlist = waitlistService.getBySubscriptionId(id);
        if (waitlistService.getMemberAmount(waitlist.getId()) != 0) {
            UUID popedMemberID = waitlistService.popMember(waitlist.getId());
            this.addMember(id, popedMemberID);

            producer.publishWaitlistPromoted(
                new WaitlistPromotedEvent(waitlist.getId(), popedMemberID, subscription.getName())
            );
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public void removePlanningFromAll(UUID planningId) {
        for (Subscription subscription : this.getAll()) {
            if (subscription.getPlanningsID().contains(planningId)) {
                subscription.getPlanningsID().remove(planningId);
                subscriptionRepository.save(subscription);

                producer.publishPlanningRemovedFromSubscription(
                    new PlanningRemovedFromSubscriptionEvent(
                            subscription.getId(),
                            planningId,
                            subscription.getMembersID())
                );
            }
        }
    }

    /**
     * Check if a Planning is valid.
     * 
     * @param planningID the planning id
     * @throws IOException
     * @throws InterruptedException
     */
    private void validatePlanning(UUID planningID) throws IOException, InterruptedException {
        HttpRequest bookingRequest = HttpRequest.newBuilder()
                .uri(URI.create(catalogServiceUrl + "/api/catalog/plannings/" + planningID))
                .GET()
                .build();
        if (httpClient.send(bookingRequest, HttpResponse.BodyHandlers.ofString()).statusCode() == 404) {
            throw new PlanningNotFoundException(planningID);
        }
    }

    /**
     * Check if all Planning refered in planningsID are valid.
     * 
     * @param planningIDs the list of Planning IDs
     * @throws IOException
     * @throws InterruptedException
     */
    private void validatePlanningList(List<UUID> planningIDs) throws IOException, InterruptedException {        
        for (UUID id : planningIDs) {
            validatePlanning(id);
        }
    }

    /**
     * Make a REST request to get the Planning dateTime.
     * 
     * @param planningID the Planning id
     * @return the Planning dateTime
     * @throws IOException
     * @throws InterruptedException
     */
    private LocalDateTime getPlanningDateTime(UUID planningID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(catalogServiceUrl + "/api/catalog/plannings/" + planningID))
            .GET()
            .build();
    
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.body());
        return LocalDateTime.parse(json.get("dateTime").toString());
    }

}
