package com.arnt.notification.services;

import java.util.List;
import java.util.UUID;

import com.arnt.notification.dto.TemplateDTO;
import com.arnt.notification.entities.Template;

public interface TemplateService {
    // Base methods related to TemplateRepository.
    List<Template> getAll();
    Template get(UUID id);
    Template save(TemplateDTO dto);
    Template update(UUID id, TemplateDTO dto);
    void delete(UUID id);

}
