package com.arnt.notification.dto;

import com.arnt.notification.entities.Template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Template Data Transfer Object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDTO {
    private String name;
    private String subject;
    private String content;

    /**
     * Convert DTO to Template.
     * 
     * @return Template entity
     */
    public Template toEntity() {
        Template template = new Template();
        template.setName(this.name);
        template.setSubject(this.subject);
        template.setContent(this.content);
        return template;
    }
}
