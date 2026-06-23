package com.arnt.user.config;

import com.arnt.user.entities.User;
import com.arnt.user.entities.Member; // Ajuste l'import selon ton projet
import com.arnt.user.repositories.UserRepository;
import com.arnt.user.repositories.MemberRepository; // Ajuste l'import si tu as un repo dédié
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository; // Injecte le repo des profils

    // Constructeur pour l'injection Spring
    public DataInitializer(UserRepository userRepository, MemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // On nettoie et on initialise si aucun utilisateur n'existe
        if (userRepository.findAll().isEmpty()) {
            
            // 1. Création de l'ID commun ou lié
            UUID userId = UUID.randomUUID();
            UUID memberId = UUID.randomUUID();

            // 2. Création du Profil Membre
            Member member = new Member();
            member.setId(memberId);
            member.setName("John");
            member.setSurname("Doe");
            member.setEmail("john.doe@gymfit.com");
            member.setUserID(userId);

            // 3. Création de l'Utilisateur (Authentification)
            User testUser = new User();
            testUser.setId(userId);
            testUser.setUsername("testgym");
            testUser.setPassword("password"); 
            testUser.setRole("MEMBER");
            testUser.setRoleID(memberId);

            // 4. Sauvegarde dans les listes en mémoire (Repositories)
            memberRepository.save(member);
            userRepository.save(testUser);

            System.out.println(">> [DATA INITIALIZER] Liens créés avec succès !");
            System.out.println("   User   : testgym / password123 (ID: " + userId + ")");
            System.out.println("   Profile: John Doe - john.doe@gymfit.com (ID: " + memberId + ")");
        }
    }
}