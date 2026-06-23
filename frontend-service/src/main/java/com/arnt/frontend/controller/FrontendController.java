package com.arnt.frontend.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
public class FrontendController {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // URL interne du conteneur User-Service dans le réseau Docker
    private final String USER_SERVICE_URL = "http://user-service:8080/api/user/auth/login";

    /**
     * 0. Affiche la page d'accueil générale (index.html)
     * URL d'accès : http://localhost/
     */
    @GetMapping("/")
    public String index(HttpSession session) {
        // Cherche le fichier src/main/resources/templates/index.html
        // Si l'utilisateur a déjà une session active, on l'aiguille directement
        String role = (String) session.getAttribute("role");

        if (role != null) {
            switch (role) {
                case "MEMBER": return "redirect:/member/home";
                case "COACH":  return "redirect:/coach/home";
                case "ADMIN":  return "redirect:/admin/dashboard";
            }
        }
        return "index";
    }

    /**
     * 1. Affiche la page de login
     */
    @GetMapping("/auth/login")
    public String showLoginPage() {
        return "auth/login";
    }

    /**
     * 2. Traite la soumission du formulaire HTML (Pure Java)
     */
    @PostMapping("/auth/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        System.out.println("\n--- [FRONTEND] Tentative de connexion pour : " + username + " ---");

        Map<String, String> loginRequest = Map.of(
            "username", username,
            "password", password
        );

        try {
            ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(
                USER_SERVICE_URL, 
                loginRequest, 
                LoginResponseDTO.class
            );

            LoginResponseDTO dto = response.getBody();

            if (dto != null) {
                System.out.println("[FRONTEND] Réponse reçue du user-service :");
                System.out.println("  -> Role   : " + dto.getRole());
                System.out.println("  -> roleID : " + dto.getRoleID());
                System.out.println("  -> Token  : " + dto.getToken());

                // Sauvegarde en session
                session.setAttribute("token", dto.getToken());
                session.setAttribute("role", dto.getRole());
                session.setAttribute("username", username);
                
                // ATTENTION : On vérifie si roleID arrive sous forme de String ou UUID
                if (dto.getRoleID() != null) {
                    session.setAttribute("roleID", UUID.fromString(dto.getRoleID()));
                }

                System.out.println("[FRONTEND] Session configurée. Redirection vers l'espace adéquat...");

                if ("MEMBER".equals(dto.getRole())) {
                    return "redirect:/member/home";
                } else if ("COACH".equals(dto.getRole())) {
                    return "redirect:/coach/home";
                } else if ("ADMIN".equals(dto.getRole())) {
                    return "redirect:/admin/dashboard";
                } else {
                    System.out.println("[FRONTEND] ATTENTION: Rôle inconnu détecté : " + dto.getRole());
                    model.addAttribute("error", "Rôle inconnu.");
                    return "auth/login";
                }
            }

        } catch (Exception e) {
            System.err.println("[FRONTEND] ERREUR pendant le POST login : " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Mot de passe ou username incorrect.");
            return "auth/login";
        }

        return "auth/login";
    }

    @GetMapping("/member/home")
    public String memberHome(HttpSession session, Model model) {
        System.out.println("\n--- [FRONTEND] Accès à /member/home demandé ---");
        
        String role = (String) session.getAttribute("role");
        UUID roleID = (UUID) session.getAttribute("roleID");

        System.out.println("  -> Attribut Session 'role'   : " + role);
        System.out.println("  -> Attribut Session 'roleID' : " + roleID);

        // Vérification de sécurité
        if (!"MEMBER".equals(role) || roleID == null) {
            System.out.println("[FRONTEND] ÉCHEC SÉCURITÉ: Redirection forcée vers /auth/login");
            return "redirect:/auth/login";
        }
        
        try {
            String memberServiceUrl = "http://user-service:8080/api/user/members/" + roleID;
            System.out.println("[FRONTEND] Requête vers user-service: GET " + memberServiceUrl);
            
            ResponseEntity<MemberResponseDTO> response = restTemplate.getForEntity(
                memberServiceUrl, 
                MemberResponseDTO.class
            );
            
            MemberResponseDTO trueMember = response.getBody();
            System.out.println("[FRONTEND] Membre récupéré avec succès : " + (trueMember != null ? trueMember.getName() : "NULL"));
            
            model.addAttribute("member", trueMember);

        } catch (Exception e) {
            System.err.println("[FRONTEND] ERREUR lors de la récupération du membre sur user-service : " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Impossible de charger votre profil.");
            model.addAttribute("member", new MemberResponseDTO(roleID, "Introuvable", "Erreur", ""));
        }
        
        model.addAttribute("mySubscriptions", java.util.List.of());
        model.addAttribute("catalogSubscriptions", java.util.List.of());

        return "member/home"; 
    }
}

/**
 * DTO interne pour mapper proprement la réponse JSON du user-service
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class LoginResponseDTO {
    private String token;
    private String role;
    private String roleID;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MemberResponseDTO {
    private UUID id;
    private String surname;
    private String name;
    private String email;
}