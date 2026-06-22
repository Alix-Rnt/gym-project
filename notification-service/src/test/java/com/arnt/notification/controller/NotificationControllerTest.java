package com.arnt.notification.controller;

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

import com.arnt.notification.dto.TemplateDTO;
import com.arnt.notification.entities.Notification;
import com.arnt.notification.entities.Template;
import com.arnt.notification.exceptions.NotificationNotFoundException;
import com.arnt.notification.exceptions.TemplateNotFoundException;
import com.arnt.notification.services.NotificationService;
import com.arnt.notification.services.TemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private NotificationService notificationService;
    @MockitoBean
    private TemplateService templateService;

    @Test
    void getNotifications_shouldReturn200() throws Exception {
        when(notificationService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/notification/notifications"))
            .andExpect(status().isOk());
    }

    @Test
    void getNotification_shouldReturn200_whenExists() throws Exception {
        UUID id = UUID.randomUUID();
        Notification notification = new Notification();
        notification.setId(id);

        when(notificationService.get(id)).thenReturn(notification);

        mockMvc.perform(get("/api/notification/notifications/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getNotification_shouldReturn404_whenNotFound() throws Exception {
        when(notificationService.get(any())).thenThrow(new NotificationNotFoundException(UUID.randomUUID()));

        mockMvc.perform(get("/api/notification/notifications/" + UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void getTemplates_shouldReturn200() throws Exception {
        when(templateService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/notification/templates"))
            .andExpect(status().isOk());
    }

    @Test
    void createTemplate_shouldReturn201() throws Exception {
        TemplateDTO dto = new TemplateDTO();
        Template template = new Template();

        when(templateService.save(any())).thenReturn(template);

        mockMvc.perform(post("/api/notification/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    void deleteTemplate_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/notification/templates/" + id))
            .andExpect(status().isNoContent());

        verify(templateService).delete(id);
    }

    @Test
    void getTemplate_shouldReturn404_whenNotFound() throws Exception {
        when(templateService.get(any())).thenThrow(new TemplateNotFoundException(UUID.randomUUID().toString()));

        mockMvc.perform(get("/api/notification/templates/" + UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }
}