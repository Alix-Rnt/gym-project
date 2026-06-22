package com.arnt.notification.services;

import java.util.List;
import java.util.UUID;

import com.arnt.notification.dto.TemplateDTO;
import com.arnt.notification.entities.Template;
import com.arnt.notification.exceptions.TemplateNotFoundException;
import com.arnt.notification.repositories.TemplateRepository;

public class TemplateServiceImpl implements TemplateService {
    private final TemplateRepository templateRepository;

    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public List<Template> getAll() {
        return templateRepository.findAll();
    }

    @Override
    public Template get(UUID id) {
        return templateRepository
                .findById(id)
                .orElseThrow(() -> new TemplateNotFoundException(id));
    }

    @Override
    public Template save(TemplateDTO dto) {
        Template template = dto.toEntity();

        return templateRepository.save(template);
    }

    @Override
    public Template update(UUID id, TemplateDTO dto) {
        Template template = this.get(id);

        template.setName(dto.getName());
        template.setSubject(dto.getSubject());
        template.setContent(dto.getContent());

        return templateRepository.save(template);
    }

    @Override
    public void delete(UUID id) {
        this.get(id);
        templateRepository.delete(id);
    }

}
