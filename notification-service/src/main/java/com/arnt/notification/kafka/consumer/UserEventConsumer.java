package com.arnt.notification.kafka.consumer;

import java.io.IOException;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.arnt.notification.client.BookingClient;
import com.arnt.notification.events.inbound.MemberSubscribedEvent;
import com.arnt.notification.events.inbound.MemberUnsubscribedEvent;
import com.arnt.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {
    private static final String SERVICE_NAME = "user-service";

    private final NotificationService notificationService;

    private final BookingClient bookingClient;

    /**
     * Consumed when a Member subscribes from a Subscription.
     * 
     * @param event MemberSubscribedEvent
     * @throws IOException
     * @throws InterruptedException
     */
    @KafkaListener(topics = "member.subscription.subscribe", groupId = SERVICE_NAME)
    public void onMemberSubscribe(MemberSubscribedEvent event) throws IOException, InterruptedException {
        notificationService.sendSubscriptionNotification(
                "SUBSCRIPTION",
                List.of(event.memberID()),
                List.of(event.memberEmail()),
                bookingClient.getSubscriptionName(event.subscriptionID()));
    }

    /**
     * Consumed when a Member unsubscribes from a Subscription.
     * 
     * @param event MemberSubscribedEvent
     * @throws IOException
     * @throws InterruptedException
     */
    @KafkaListener(topics = "member.subscription.unsubscribe", groupId = SERVICE_NAME)
    public void onMemberUnsubscribe(MemberUnsubscribedEvent event) throws IOException, InterruptedException {
        notificationService.sendSubscriptionNotification(
                "UNSUBSCRIPTION",
                List.of(event.memberID()),
                List.of(event.memberEmail()),
                bookingClient.getSubscriptionName(event.subscriptionID()));
    }
    
}
