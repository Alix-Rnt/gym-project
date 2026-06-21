package com.arnt.catalog.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.arnt.catalog.dto.PlanningDTO;
import com.arnt.catalog.entities.Planning;
import com.arnt.catalog.status.PlanningStatus;

/**
 * PlanningService implements methods related to Planning management.
 */
public interface PlanningService {
    // Base methods related to PlanningRepository.
    List<Planning> getAll();
    Planning get(UUID id);
    Planning save(PlanningDTO dto) throws IOException, InterruptedException;
    Planning update(UUID id, PlanningDTO dto) throws IOException, InterruptedException;
    void delete(UUID id);

    /**
     * Update Planning status
     * 
     * @param id the Planning id
     * @param status the next status
     * @return the updated Planning
     */
    Planning updateStatus(UUID id, PlanningStatus status);


}
