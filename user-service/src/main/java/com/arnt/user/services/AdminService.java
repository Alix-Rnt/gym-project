package com.arnt.user.services;

import java.util.Collection;
import java.util.UUID;
import com.arnt.user.dto.AdminDTO;
import com.arnt.user.entities.Admin;

public interface AdminService {
    Collection<Admin> getAll();
    Admin get(UUID id);
    Admin save(AdminDTO dto);
    Admin update(UUID id, AdminDTO dto);
    void delete(UUID id);
}