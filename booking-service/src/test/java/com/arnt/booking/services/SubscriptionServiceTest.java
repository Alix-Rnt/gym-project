package com.arnt.booking.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arnt.booking.entities.Subscription;
import com.arnt.booking.enums.SubscriptionStatus;
import com.arnt.booking.exceptions.SubscriptionNotFoundException;
import com.arnt.booking.kafka.producer.BookingEventProducer;
import com.arnt.booking.repositories.SubscriptionRepository;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private WaitlistServiceImpl waitlistService;
    @Mock
    private BookingEventProducer producer;
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    void addMember_shouldAddToSubscription_whenCapacityAvailable() throws IOException, InterruptedException {
        UUID memberId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();

        Subscription sub = new Subscription();
        sub.setCapacity(10);
        sub.setMembersID(new ArrayList<>());

        when(subscriptionRepository.findById(any())).thenReturn(Optional.of(sub));

        subscriptionService.addMember(subscriptionId, memberId);

        assertEquals(1, sub.getMembersID().size());
        assertTrue(sub.getMembersID().contains(memberId));
        verify(subscriptionRepository).save(sub);
        verify(waitlistService, never()).addMember(any(), any());
    }

    @Test
    void addMember_shouldAddToWaitlist_whenCapacityFull() throws IOException, InterruptedException {
        UUID memberId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();

        Subscription sub = new Subscription();
        sub.setCapacity(1);
        sub.setMembersID(new ArrayList<>(List.of(UUID.randomUUID())));

        when(subscriptionRepository.findById(any())).thenReturn(Optional.of(sub));

        subscriptionService.addMember(subscriptionId, memberId);

        verify(waitlistService).addMemberBySubscriptionId(subscriptionId, memberId);
        assertEquals(1, sub.getMembersID().size());
    }

    @Test
    void removePlanning_shouldRemovePlanningFromSubscription() throws IOException, InterruptedException {
        UUID subscriptionId = UUID.randomUUID();
        UUID planningId = UUID.randomUUID();

        Subscription sub = new Subscription();
        sub.setPlanningsID(new ArrayList<>(List.of(planningId)));

        when(subscriptionRepository.findById(any())).thenReturn(Optional.of(sub));

        subscriptionService.removePlanning(subscriptionId, planningId);

        assertEquals(0, sub.getPlanningsID().size());
        verify(subscriptionRepository).save(sub);
        verify(producer).publishPlanningRemovedFromSubscription(any());
    }

    @Test
    void updateStatus_shouldPublishExpiredEvent_whenStatusIsExpired() {
        UUID subscriptionId = UUID.randomUUID();

        Subscription sub = new Subscription();
        sub.setId(subscriptionId);
        sub.setMembersID(new ArrayList<>(List.of(UUID.randomUUID())));

        when(subscriptionRepository.findById(any())).thenReturn(Optional.of(sub));

        subscriptionService.updateStatus(subscriptionId, SubscriptionStatus.EXPIRED);

        verify(producer).publishSubscriptionExpired(any());
        verify(producer, never()).publishSubscriptionInvalidated(any());
    }

    @Test
    void updateStatus_shouldPublishInvalidatedEvent_whenStatusIsInvalid() {
        UUID subscriptionId = UUID.randomUUID();

        Subscription sub = new Subscription();
        sub.setId(subscriptionId);
        sub.setMembersID(new ArrayList<>(List.of(UUID.randomUUID())));

        when(subscriptionRepository.findById(any())).thenReturn(Optional.of(sub));

        subscriptionService.updateStatus(subscriptionId, SubscriptionStatus.INVALID);

        verify(producer).publishSubscriptionInvalidated(any());
        verify(producer, never()).publishSubscriptionExpired(any());
    }

    @Test
    void addMember_shouldThrowException_whenSubscriptionNotFound() {
        when(subscriptionRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, 
            () -> subscriptionService.addMember(UUID.randomUUID(), UUID.randomUUID()));
    }
    
}
