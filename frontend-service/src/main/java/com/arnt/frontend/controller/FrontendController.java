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

@Controller
public class FrontendController {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // URL interne du conteneur User-Service dans le réseau Docker
    private final String USER_SERVICE_URL = "http://user-service:8080/api/user/auth/login";

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

        // On prépare le corps de la requête (Le DTO attendu par ton user-service)
        Map<String, String> loginRequest = Map.of(
            "username", username,
            "password", password
        );

        try {
            // Appel synchrone au microservice User
            ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(
                USER_SERVICE_URL, 
                loginRequest, 
                LoginResponseDTO.class
            );

            LoginResponseDTO dto = response.getBody();

            if (dto != null) {
                // Stockage des informations critiques dans la session Java du serveur
                session.setAttribute("token", dto.getToken());
                session.setAttribute("role", dto.getRole());
                session.setAttribute("roleId", dto.getRoleId());
                session.setAttribute("username", username);

                // Redirection côté serveur selon le rôle reçu
                switch (dto.getRole()) {
                    case "ADMIN":
                        return "redirect:/admin/dashboard";
                    case "COACH":
                        return "redirect:/coach/home";
                    case "MEMBER":
                        return "redirect:/member/home";
                    default:
                        model.addAttribute("error", "Rôle inconnu.");
                        return "auth/login";
                }
            }

        } catch (Exception e) {
            // Si le serveur renvoie une 401, 404 ou crash, on l'intercepte ici
            model.addAttribute("error", "Mot de passe ou username incorrect.");
            return "auth/login"; // Réaffiche la page de login avec le message d'erreur
        }

        model.addAttribute("error", "Une erreur inattendue est survenue.");
        return "auth/login";
    }

    // --- Pages de destination temporaires (Vides pour tes tests) ---

    @GetMapping("/member/home")
    public String memberHome(HttpSession session, Model model) {
        // 1. Sécurité : si pas connecté ou pas MEMBER, retour au login
        if (!"MEMBER".equals(session.getAttribute("role"))) {
            return "redirect:/auth/login";
        }
        
        // 2. Simulation temporaire de l'objet "member" pour éviter le crash Thymeleaf
        Map<String, String> fakeMember = Map.of(
            "name", "Utilisateur",
            "surname", "De Test",
            "email", "testgym@example.com"
        );
        model.addAttribute("member", fakeMember);
        
        // 3. On passe aussi des listes vides temporaires pour les abonnements
        model.addAttribute("mySubscriptions", java.util.List.of());
        model.addAttribute("catalogSubscriptions", java.util.List.of());

        return "member/home";
    }

    @GetMapping("/coach/home")
    public String coachHome(HttpSession session, Model model) {
        if (!"COACH".equals(session.getAttribute("role"))) return "redirect:/auth/login";
        return "coach/home";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("role"))) return "redirect:/auth/login";
        return "admin/dashboard";
    }
}

/**
 * DTO interne pour mapper proprement la réponse JSON du user-service
 */
class LoginResponseDTO {
    private String token;
    private String role;
    private String roleId;

    // Getters et Setters obligatoires pour le mapping automatique
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
}