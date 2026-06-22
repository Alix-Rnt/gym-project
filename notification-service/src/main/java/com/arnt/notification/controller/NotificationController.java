package com.arnt.notification.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arnt.notification.dto.TemplateDTO;
import com.arnt.notification.entities.Notification;
import com.arnt.notification.entities.Template;
import com.arnt.notification.services.NotificationService;
import com.arnt.notification.services.TemplateService;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final TemplateService templateService;

    /**
     * NotificationController constructor.
     * 
     * @param notificationService
     * @param templateService
     */
    public NotificationController(
            NotificationService notificationService,
            TemplateService templateService
    ) {
        this.notificationService = notificationService;
        this.templateService = templateService;
    }

    /*
        =====================
        Notification requests
        =====================
    */

    /**
     * List all Notification.
     * GET /notifications
     * 
     * @return a list of every Notification
     */
    @GetMapping("/notifications")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Notification> getNotifications() {
        return notificationService.getAll();
    }

    /**
     * Get one Notification.
     * GET /notifications/{id}
     * 
     * @param id the Notification id
     * @return the found Notification
     */
    @GetMapping("/notifications/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Notification getNotification(@PathVariable UUID id) {
        return notificationService.get(id);
    }

    /*
        =================
        Template requests
        =================
    */

    /**
     * List all Template.
     * GET /templates
     * 
     * @return a list of every Template
     */
    @GetMapping("/templates")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Template> getTemplates() {
        return templateService.getAll();
    }

    /**
     * Create a Template.
     * POST /templates
     * 
     * @param dto the Template DTO body
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("/templates")
    @ResponseStatus(HttpStatus.CREATED)
    public Template createTemplate(@RequestBody TemplateDTO dto) {
        return templateService.save(dto);
    }

    /**
     * Get one Template.
     * GET /templates/{id}
     * 
     * @param id the Template id
     * @return the found Template
     */
    @GetMapping("/templates/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Template getTemplate(@PathVariable UUID id) {
        return templateService.get(id);
    }

    /**
     * Update one Template.
     * PUT /templates/{id}
     * 
     * @param dto the new information
     * @param id the Template id
     */
    @PutMapping("/templates/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Template updateTemplate(@RequestBody TemplateDTO dto, @PathVariable UUID id) {
        return templateService.update(id, dto);
    }

    /**
     * Delete one Template.
     * DELETE /templates/{id}
     * 
     * @param id the Template id
     */
    @DeleteMapping("/templates/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTemplate(@PathVariable UUID id) {
        templateService.delete(id);
    }
    
}
