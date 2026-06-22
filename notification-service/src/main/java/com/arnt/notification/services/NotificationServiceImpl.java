package com.arnt.notification.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.arnt.notification.dto.NotificationDTO;
import com.arnt.notification.entities.Notification;
import com.arnt.notification.entities.Template;
import com.arnt.notification.exceptions.NotificationNotFoundException;
import com.arnt.notification.exceptions.TemplateNotFoundException;
import com.arnt.notification.repositories.NotificationRepository;
import com.arnt.notification.repositories.TemplateRepository;
import com.arnt.notification.types.NotificationType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final TemplateRepository templateRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            TemplateRepository templateRepository) {
        this.notificationRepository = notificationRepository;
        this.templateRepository = templateRepository;
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification get(UUID id) {
        return notificationRepository
                .findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    @Override
    public Notification save(NotificationDTO dto) {
        Notification notification = dto.toEntity();

        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(UUID id, NotificationDTO dto) {
        Notification notification = this.get(id);

        notification.setType(dto.getType());
        notification.setSubject(dto.getSubject());
        notification.setContent(dto.getContent());
        notification.setSendTime(dto.getSendTime());
        notification.setMembersID(dto.getMembersID());
        notification.setTemplateID(dto.getTemplateID());

        return notificationRepository.save(notification);
    }

    @Override
    public void delete(UUID id) {
        this.get(id);
        notificationRepository.delete(id);
    }

    @Override
    public void sendSubscriptionNotification(
            String templateName,
            List<UUID> membersId,
            List<String> emails,
            String subscriptionName) throws IOException, InterruptedException {
        this.createAndSend(
            templateName,
            NotificationType.EMAIL,
            membersId,
            emails,
            Map.of("subscriptionName", subscriptionName)
        );
    }
    
    /**
     * Generate a Notification from a Template and send it.
     * 
     * @param templateName the Template name
     * @param type the Notification type
     * @param membersID the Member ids
     * @param variables information of the Notification
     */
    private void createAndSend(
            String templateName,
            NotificationType type,
            List<UUID> membersID,
            List<String> infos,
            Map<String, String> variables) {

        Template template = templateRepository
                .findByName(templateName)
                .orElseThrow(() -> TemplateNotFoundException.fromName(templateName));
        
        String content = resolveVariables(template.getContent(), variables);
        String subject = resolveVariables(template.getSubject(), variables);

        Notification notification = new Notification();
        notification.setTemplateID(template.getId());
        notification.setType(type);
        notification.setSubject(subject);
        notification.setContent(content);
        notification.setSendTime(LocalDateTime.now());
        notification.setMembersID(membersID);

        switch (type) {
            case EMAIL -> infos.forEach(email -> sendEmail(email, subject, content));
            case SMS -> infos.forEach(sms -> sendSMS(sms, content));
            case UNKNOWN -> throw new UnsupportedOperationException("Unimplemented case: " + type);
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        }

        notificationRepository.save(notification);
    }

    /**
     * Change tags in text to the corresponding value in the variable map.
     * 
     * @param text the original text with tags
     * @param variables the tags and values mapping
     * @return the modified text
     */
    private String resolveVariables(String text, Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            text = text.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return text;
    }

    /* EMAIL */

    /**
     * Send an email with a subject and a content.
     * 
     * @param email the email to send to
     * @param subject the email subject
     * @param content the email content
     */
    private void sendEmail(String email, String subject, String content) {
        log.info("Email sent to : {} \nSubject : {} \nContent : {}", email, subject, content);
    }

    /* SMS */

    /**
     * Send an email with a subject and a content.
     * 
     * @param email the email to send to
     * @param subject the email subject
     * @param content the email content
     */
    private void sendSMS(String phone, String content) {
        log.info("Message sent to : {} \nContent : {}", phone, content);
    }

}
