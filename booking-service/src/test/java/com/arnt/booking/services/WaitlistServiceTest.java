package com.arnt.booking.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arnt.booking.entities.Waitlist;
import com.arnt.booking.exceptions.WaitlistEmptyException;
import com.arnt.booking.repositories.WaitlistRepository;

@ExtendWith(MockitoExtension.class)
public class WaitlistServiceTest {

    @Mock
    private WaitlistRepository waitlistRepository;
    @InjectMocks
    private WaitlistServiceImpl waitlistService;

    @Test
    void popMember_shouldReturnOldestMember() {
        UUID subscriptionId = UUID.randomUUID();
        UUID firstMemberId = UUID.randomUUID();
        UUID secondMemberId = UUID.randomUUID();

        Waitlist waitlist = new Waitlist();
        waitlist.setSubscriptionID(subscriptionId);
        waitlist.setMembersTimestamp(new HashMap<>(Map.of(
            firstMemberId, Timestamp.valueOf(LocalDateTime.now().minusHours(2)),
            secondMemberId, Timestamp.valueOf(LocalDateTime.now())
        )));

        when(waitlistRepository.findById(any())).thenReturn(Optional.of(waitlist));

        UUID promoted = waitlistService.popMember(subscriptionId);

        assertEquals(firstMemberId, promoted);
        assertFalse(waitlist.getMembersTimestamp().containsKey(firstMemberId));
        verify(waitlistRepository).save(waitlist);
    }

    @Test
    void popMember_shouldThrowException_whenWaitlistIsEmpty() {
        UUID subscriptionId = UUID.randomUUID();

        Waitlist waitlist = new Waitlist();
        waitlist.setSubscriptionID(subscriptionId);
        waitlist.setMembersTimestamp(new HashMap<>());

        when(waitlistRepository.findById(any())).thenReturn(Optional.of(waitlist));

        assertThrows(WaitlistEmptyException.class, () -> waitlistService.popMember(subscriptionId));
    }
}