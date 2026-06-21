package com.arnt.booking.kafka.consumer;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.arnt.booking.events.inbound.MemberSubscribedEvent;
import com.arnt.booking.events.inbound.MemberUnsubscribedEvent;
import com.arnt.booking.services.SubscriptionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberEventConsumer {
    private final SubscriptionService subscriptionService;

    @KafkaListener(topics = "member.subscription.subscribe", groupId = "user-service")
    public void onMemberSubscribe(MemberSubscribedEvent event) throws IOException, InterruptedException {
        log.info("Member subsciption recieved : memberId = {} ; subscriptionId = {}", event.memberID(), event.subscriptionID());

        subscriptionService.addMember(event.memberID(), event.subscriptionID());
    }

    @KafkaListener(topics = "member.subscription.unsubscribe", groupId = "user-service")
    public void onMemberUnsubscribe(MemberUnsubscribedEvent event) throws IOException, InterruptedException {
        log.info("Member unsubsciption recieved : memberId = {} ; subscriptionId = {}", event.memberID(), event.subscriptionID());

        subscriptionService.removeMember(event.memberID(), event.subscriptionID());
    }
}
