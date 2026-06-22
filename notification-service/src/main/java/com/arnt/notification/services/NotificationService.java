package com.arnt.notification.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.arnt.notification.dto.NotificationDTO;
import com.arnt.notification.entities.Notification;

/**
 * NotificationService implements methods related to Notification management.
 */
public interface NotificationService {
    // Base methods related to NotificationRepository.
    List<Notification> getAll();
    Notification get(UUID id);
    Notification save(NotificationDTO dto);
    Notification update(UUID id, NotificationDTO dto);
    void delete(UUID id);

    /**
     * Send a Notification.
     * 
     * @param templateName the Template name
     * @param membersId the Member ids
     * @param email the Member email
     * @param subscriptionName the Subscription name
     * @throws IOException
     * @throws InterruptedException
     */
    void sendSubscriptionNotification(
            String templateName,
            List<UUID> membersId,
            List<String> emails,
            String subscriptionName) throws IOException, InterruptedException;

}
