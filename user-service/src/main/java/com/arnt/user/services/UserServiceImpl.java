package com.arnt.user.services;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.arnt.user.dto.LoginRequestDTO;
import com.arnt.user.dto.LoginResponseDTO;
import com.arnt.user.dto.RegistrationDTO;
import com.arnt.user.entities.User;
import com.arnt.user.entities.Member;
import com.arnt.user.entities.Coach;
import com.arnt.user.repositories.UserRepository;
import com.arnt.user.repositories.MemberRepository;
import com.arnt.user.repositories.CoachRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final CoachRepository coachRepository;

    // Injection par constructeur
    public UserServiceImpl(UserRepository userRepository, MemberRepository memberRepository, CoachRepository coachRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.coachRepository = coachRepository;
    }

    @Override
    public User registerNewUser(RegistrationDTO dto) {
        // 1. Vérification de l'unicité du Username
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + dto.getUsername() + "' is already taken."); 
        }

        // 2. Création et configuration de l'entité User de sécurité
        User user = dto.toUserEntity();
        UUID generatedUserId = UUID.randomUUID();
        user.setId(generatedUserId);
        // Note: Idéalement, hacher le mot de passe ici si tu ajoutes un BCryptPasswordEncoder plus tard

        // 3. Création du profil spécifique selon le rôle
        UUID generatedRoleId = UUID.randomUUID();
        
        if ("MEMBER".equals(dto.getRole())) {
            Member member = dto.toMemberEntity();
            member.setId(generatedRoleId);
            member.setUserID(generatedUserId); // Clé étrangère vers le User
            memberRepository.save(member);
        } else if ("COACH".equals(dto.getRole())) {
            Coach coach = dto.toCoachEntity();
            coach.setId(generatedRoleId);
            coach.setUserID(generatedUserId); // Clé étrangère vers le User
            coachRepository.save(coach);
        } else {
            throw new IllegalArgumentException("Unknown or unsupported User Role.");
        }

        // 4. On lie le rôle ID généré à notre entité User globale
        user.setRoleID(generatedRoleId);

        // 5. Sauvegarde finale du User
        return userRepository.save(user);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        // 1. Recherche de l'utilisateur par son username
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password."));

        // 2. Vérification du mot de passe (comparaison brute pour l'instant)
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        // 3. Simulation de la génération d'un jeton JWT
        // Dans une vraie app, on utiliserait une lib comme io.jsonwebtoken (jjwt)
        String mockJwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mockTokenForUser." + user.getUsername();

        // 4. Construction et renvoi du LoginResponseDTO
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(mockJwtToken);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setRoleID(user.getRoleID());

        return response;
    }

    @Override
    public void logout() {
        // En mode Stateless (JWT), le serveur n'a rien à faire. 
        // C'est le client qui détruit le token. On laisse la méthode vide ou pour logs.
    }

    @Override
    public LoginResponseDTO refresh() {
        // Logique de rafraîchissement (généralement basée sur un Refresh Token)
        return null; 
    }
}