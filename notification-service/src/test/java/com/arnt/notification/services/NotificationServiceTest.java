package com.arnt.notification.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.arnt.notification.entities.Notification;
import com.arnt.notification.entities.Template;
import com.arnt.notification.exceptions.TemplateNotFoundException;
import com.arnt.notification.repositories.NotificationRepository;
import com.arnt.notification.repositories.TemplateRepository;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private TemplateRepository templateRepository;
    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void sendSubscriptionNotification_shouldLogEmail_whenTemplateExists() throws IOException, InterruptedException {
        Template template = new Template();
        template.setId(UUID.randomUUID());
        template.setSubject("Subscription to {{subscriptionName}}");
        template.setContent("You are subscribed to {{subscriptionName}}");

        when(templateRepository.findByName(any())).thenReturn(Optional.of(template));

        notificationService.sendSubscriptionNotification(
            "SUBSCRIPTION",
            List.of(UUID.randomUUID()),
            List.of("member@email.com"),
            "Yoga Premium"
        );

        verify(templateRepository).findByName("SUBSCRIPTION");
    }

    @Test
    void sendSubscriptionNotification_shouldThrow_whenTemplateNotFound() {
        when(templateRepository.findByName(any())).thenReturn(Optional.empty());

        assertThrows(TemplateNotFoundException.class, () ->
            notificationService.sendSubscriptionNotification(
                "INEXISTANT",
                List.of(UUID.randomUUID()),
                List.of("membre@email.com"),
                "Yoga Premium"
            )
        );
    }

    @Test
    void sendSubscriptionNotification_shouldResolveVariables() throws IOException, InterruptedException {
        Template template = new Template();
        template.setId(UUID.randomUUID());
        template.setSubject("Subscription to {{subscriptionName}}");
        template.setContent("You are subscribed to {{subscriptionName}}");

        when(templateRepository.findByName(any())).thenReturn(Optional.of(template));

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);

        notificationService.sendSubscriptionNotification(
            "SUBSCRIPTION",
            List.of(UUID.randomUUID()),
            List.of("member@email.com"),
            "Yoga Premium"
        );

        verify(notificationRepository).save(captor.capture());
        assertEquals("Subscription to Yoga Premium", captor.getValue().getSubject());
        assertEquals("You are subscribed to Yoga Premium", captor.getValue().getContent());
}
}