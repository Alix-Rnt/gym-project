package com.arnt.booking.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.arnt.booking.dto.SubscriptionDTO;
import com.arnt.booking.entities.Subscription;
import com.arnt.booking.exceptions.PlanningNotFoundException;
import com.arnt.booking.exceptions.SubscriptionNotFoundException;
import com.arnt.booking.repositories.SubscriptionRepository;
import com.arnt.booking.status.SubscriptionStatus;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("$(catalog.service.ulr)")
    private String catalogServiceUrl;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
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

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription addPlanning(UUID id, UUID planningId) {
        Subscription subscription = this.get(id);

        subscription.getPlanningsID().add(planningId);

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription removePlanning(UUID id, UUID planningId) {
        Subscription subscription = this.get(id);

        subscription.getPlanningsID().remove(planningId);

        return subscriptionRepository.save(subscription);
    }

    /**
     * Checks if all Planning refered in planningsID are valid.
     * 
     * @param planningIDs the list of Planning IDs
     * @throws IOException
     * @throws InterruptedException
     */
    private void validatePlanningList(List<UUID> planningIDs) throws IOException, InterruptedException {        
        for (UUID id : planningIDs) {
            HttpRequest bookingRequest = HttpRequest.newBuilder()
                    .uri(URI.create(catalogServiceUrl + "/api/catalog/plannings" + id))
                    .GET()
                    .build();
            if (httpClient.send(bookingRequest, HttpResponse.BodyHandlers.ofString()).statusCode() == 404) {
                throw new PlanningNotFoundException(id);
            }
        }
    }

}
