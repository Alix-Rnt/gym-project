package com.arnt.user.services;

import java.util.Collection;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.arnt.user.dto.AdminDTO;
import com.arnt.user.entities.Admin;
import com.arnt.user.repositories.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Collection<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin get(UUID id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + id));
    }

    @Override
    public Admin save(AdminDTO dto) {
        // En microservices, la conversion classique toEntity()
        // Si l'id ou le userID a été injecté par le UserService lors d'un ajout manuel, on le conserve.
        Admin admin = new Admin();
        
        if (admin.getId() == null) {
            admin.setId(UUID.randomUUID());
        }
        
        // Note: Si ton AdminDTO transmet le userID, on le mappe ici
        // admin.setUserID(dto.getUserID());
        
        return adminRepository.save(admin);
    }

    @Override
    public Admin update(UUID id, AdminDTO dto) {
        // Comme un admin ne contient pas d'informations de profil (nom, prénom),
        // l'update est ici minimaliste. On s'assure juste que l'entité existe.
        Admin existingAdmin = get(id);
        
        // Si tu ajoutes plus tard des champs spécifiques aux admins (ex: niveau d'accès),
        // c'est ici qu'ils seront mis à jour.
        
        return existingAdmin;
    }

    @Override
    public void delete(UUID id) {
        adminRepository.delete(id);
    }
}