package com.arnt.notification.kafka.consumer;

import java.io.IOException;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.arnt.notification.client.UserClient;
import com.arnt.notification.events.inbound.SubscriptionExpiredEvent;
import com.arnt.notification.events.inbound.WaitlistPromotedEvent;
import com.arnt.notification.services.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventConsumer {
    private static final String SERVICE_NAME = "booking-service";

    private final NotificationService notificationService;

    private final UserClient userClient;

    @KafkaListener(topics = "booking.waitlist.promoted", groupId = SERVICE_NAME)
    public void onWaitlistPromote(WaitlistPromotedEvent event) throws IOException, InterruptedException {
        notificationService.sendSubscriptionNotification(
                "PROMOTION",
                List.of(event.memberID()),
                userClient.getMemberEmails(List.of(event.memberID())),
                event.subscriptionName());
    }

    @KafkaListener(topics = "booking.subscription.expired", groupId = SERVICE_NAME)
    public void onSubscriptionExpire(SubscriptionExpiredEvent event) throws IOException, InterruptedException {
        notificationService.sendSubscriptionNotification(
                "EXPIRED",
                event.membersID(),
                userClient.getMemberEmails(event.membersID()),
                event.subscriptionName());
    }

    @KafkaListener(topics = "booking.subscription.invalidated", groupId = SERVICE_NAME)
    public void onSubscriptionInvalid(SubscriptionExpiredEvent event) throws IOException, InterruptedException {
        notificationService.sendSubscriptionNotification(
                "INVALIDATED",
                event.membersID(),
                userClient.getMemberEmails(event.membersID()),
                event.subscriptionName());
    }
    
    @KafkaListener(topics = "booking.planning.removed", groupId = SERVICE_NAME)
    public void onSubscriptionRemove(SubscriptionExpiredEvent event) throws IOException, InterruptedException {
        notificationService.sendSubscriptionNotification(
                "REMOVED",
                event.membersID(),
                userClient.getMemberEmails(event.membersID()),
                event.subscriptionName());
    }
}
