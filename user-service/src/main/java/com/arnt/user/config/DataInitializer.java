package com.arnt.user.config;

import com.arnt.user.entities.User;
import com.arnt.user.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // CORRECTION 1 : On utilise findAll().isEmpty() à la place de count()
        if (userRepository.findAll().isEmpty()) {
            User testUser = new User();
            testUser.setId(UUID.randomUUID()); // Si ton entité ne génère pas l'UUID tout seul
            testUser.setUsername("testgym");
            testUser.setPassword("password"); 
            
            // CORRECTION 2 : On passe l'Enum plutôt qu'une String
            testUser.setRole("MEMBER"); 

            userRepository.save(testUser);
            System.out.println(">> [DATA INITIALIZER] Utilisateur de test créé : testgym / password");
        }
    }
}