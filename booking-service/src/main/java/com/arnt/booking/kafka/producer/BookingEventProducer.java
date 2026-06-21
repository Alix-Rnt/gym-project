package com.arnt.booking.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.arnt.booking.events.outbound.PlanningRemovedFromSubscriptionEvent;
import com.arnt.booking.events.outbound.SubscriptionExpiredEvent;
import com.arnt.booking.events.outbound.SubscriptionInvalidatedEvent;
import com.arnt.booking.events.outbound.WaitlistPromotedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publisPlanningRemovedFromSubscription(PlanningRemovedFromSubscriptionEvent event) {
        kafkaTemplate.send("booking.planning.removed", event.subscriptionId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish booking.planning.removed : {}", ex.getMessage());
                }
            });
    }

    public void publishWaitlistPromoted(WaitlistPromotedEvent event) {
        kafkaTemplate.send("booking.waitlist.promoted", event.waitlistID().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish booking.waitlist.promoted : {}", ex.getMessage());
                }
            });
    }

    public void publishSubscriptionExpired(SubscriptionExpiredEvent event) {
        kafkaTemplate.send("booking.subscription.expired", event.subscriptionId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish booking.subscription.expired : {}", ex.getMessage());
                }
            });
    }

    public void publishSubscriptionInvalidated(SubscriptionInvalidatedEvent event) {
        kafkaTemplate.send("booking.subscription.invalidated", event.subscriptionId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish booking.subscription.invalidated : {}", ex.getMessage());
                }
            });
    }
}
