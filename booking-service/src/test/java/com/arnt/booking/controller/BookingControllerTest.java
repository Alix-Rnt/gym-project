package com.arnt.booking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.arnt.booking.dto.SubscriptionDTO;
import com.arnt.booking.entities.Subscription;
import com.arnt.booking.enums.SubscriptionStatus;
import com.arnt.booking.exceptions.SubscriptionNotFoundException;
import com.arnt.booking.services.SubscriptionService;
import com.arnt.booking.services.WaitlistService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SubscriptionService subscriptionService;
    @MockitoBean
    private WaitlistService waitlistService;

    @Test
    void getSubscriptions_shouldReturn200() throws Exception {
        when(subscriptionService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/booking/subscriptions"))
            .andExpect(status().isOk());
    }

    @Test
    void getSubscription_shouldReturn200_whenExists() throws Exception {
        UUID id = UUID.randomUUID();
        Subscription sub = new Subscription();
        sub.setId(id);

        when(subscriptionService.get(id)).thenReturn(sub);

        mockMvc.perform(get("/api/booking/subscriptions/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getSubscription_shouldReturn404_whenNotFound() throws Exception {
        when(subscriptionService.get(any())).thenThrow(new SubscriptionNotFoundException(UUID.randomUUID()));

        mockMvc.perform(get("/api/booking/subscriptions/" + UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void createSubscription_shouldReturn201() throws Exception {
        SubscriptionDTO dto = new SubscriptionDTO();
        Subscription sub = new Subscription();

        when(subscriptionService.save(any())).thenReturn(sub);

        mockMvc.perform(post("/api/booking/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    void deleteSubscription_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/booking/subscriptions/" + id))
            .andExpect(status().isNoContent());

        verify(subscriptionService).delete(id);
    }

    @Test
    void updateStatus_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        Subscription sub = new Subscription();

        when(subscriptionService.updateStatus(any(), any())).thenReturn(sub);

        mockMvc.perform(patch("/api/booking/subscriptions/" + id + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(SubscriptionStatus.EXPIRED)))
            .andExpect(status().isOk());
    }
}