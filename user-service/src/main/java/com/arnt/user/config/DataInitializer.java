package com.arnt.user.config;

import com.arnt.user.entities.User;
import com.arnt.user.entities.Member;
import com.arnt.user.entities.Coach;
import com.arnt.user.repositories.UserRepository;
import com.arnt.user.repositories.MemberRepository;
import com.arnt.user.repositories.CoachRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final CoachRepository coachRepository;

    // Constructeur pour l'injection Spring
    public DataInitializer(UserRepository userRepository, MemberRepository memberRepository, CoachRepository coachRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.coachRepository = coachRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // On nettoie et on initialise si aucun utilisateur n'existe
        if (userRepository.findAll().isEmpty()) {
            
            // CREATION D'UN MEMBRE
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
            member.setSubscriptionList(new ArrayList<UUID>());
            member.setWaitlistList(new ArrayList<UUID>());

            // 3. Création de l'Utilisateur (Authentification)
            User testUser = new User();
            testUser.setId(userId);
            testUser.setUsername("member");
            testUser.setPassword("p"); 
            testUser.setRole("MEMBER");
            testUser.setRoleID(memberId);

            // 4. Sauvegarde dans les listes en mémoire (Repositories)
            memberRepository.save(member);
            userRepository.save(testUser);

            System.out.println(">> [DATA INITIALIZER] Liens créés avec succès !");
            System.out.println("   User   : member / p (ID: " + userId + ")");
            System.out.println("   Profile: John Doe - john.doe@gymfit.com (ID: " + memberId + ")");

            // CREATION D'UN COACH
            // 1. Création de l'ID commun ou lié
            UUID coachUserId = UUID.randomUUID();
            UUID coachId = UUID.randomUUID();

            // 2. Création du Profil Coach
            Coach coach = new Coach();
            coach.setId(coachId);
            coach.setName("John");
            coach.setSurname("John");
            coach.setEmail("john.john@gymfit.com");
            coach.setUserID(coachUserId);
            coach.setLessonList(new ArrayList<UUID>());
            coach.setSubscriptionList(new ArrayList<UUID>());

            // 3. Création de l'Utilisateur (Authentification)
            User coachUser = new User();
            coachUser.setId(coachUserId);
            coachUser.setUsername("coach");
            coachUser.setPassword("p"); 
            coachUser.setRole("COACH");
            coachUser.setRoleID(coachId);

            // 4. Sauvegarde dans les listes en mémoire (Repositories)
            coachRepository.save(coach);
            userRepository.save(coachUser);

            System.out.println(">> [DATA INITIALIZER] Liens créés avec succès !");
            System.out.println("   User   : coach / p (ID: " + coachUserId + ")");
            System.out.println("   Profile: John John - john.john@gymfit.com (ID: " + coachId + ")");
        }
    }
}