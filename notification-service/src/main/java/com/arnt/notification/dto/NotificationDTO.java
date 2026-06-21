package com.arnt.notification.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.arnt.notification.entities.Notification;
import com.arnt.notification.types.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notification Data Transfer Object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private NotificationType type;
    private String subject;
    private String content;
    private LocalDateTime sendTime;
    private List<UUID> membersID;
    private UUID templateID;
    
    /**
     * Convert DTO to Notification.
     * 
     * @return Notification entity
     */
    public Notification toEntity() {
        Notification notification = new Notification();
        notification.setType(this.type);
        notification.setSubject(this.subject);
        notification.setContent(this.content);
        notification.setSendTime(this.sendTime);
        notification.setMembersID(this.membersID);
        notification.setTemplateID(this.templateID);
        return notification;
    }
}
