package com.arnt.booking.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.arnt.booking.events.inbound.PlanningCancelledEvent;
import com.arnt.booking.services.SubscriptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogEventConsumer {
    private final SubscriptionService subscriptionService;

    @KafkaListener(topics = "catalog.planning.cancelled", groupId = "booking-service")
    public void onCourseCancelled(PlanningCancelledEvent event) {
        log.info("Planning cancelled recieved : planningId={}", event.planningID());

        subscriptionService.removePlanningFromAll(event.planningID());
    }
}