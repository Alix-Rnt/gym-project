package com.arnt.frontend.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;
import java.util.List;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Controller
public class FrontendController {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // URL interne du conteneur User-Service dans le réseau Docker
    private final String USER_SERVICE_URL = "http://user-service:8080/api/user/auth/login";
    // URL de ton microservice pour l'inscription
    private final String USER_REGISTER_URL = "http://user-service:8080/api/user/auth/register";

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
     * 1. Affiche la page de register
     */
    @GetMapping("/auth/register")
    public String showRegisterPage() {
        return "auth/register";
    }

    /**
     * 1. Affiche la page de register
     */
    @PostMapping("/auth/register")
    public String handleRegister(
            @ModelAttribute RegistrationDTO registrationDto,
            HttpSession session,
            Model model) {System.out.println("\n--- [FRONTEND] Tentative d'inscription pour : " + registrationDto.getUsername() + " ---");
        System.out.println("  -> Nom/Prénom : " + registrationDto.getSurname() + " " + registrationDto.getName());
        System.out.println("  -> Rôle choisi : " + registrationDto.getRole());

        try {
            // Envoi du DTO complet encapsulé automatiquement en JSON par RestTemplate
            ResponseEntity<Void> response = restTemplate.postForEntity(
                USER_REGISTER_URL, 
                registrationDto, 
                Void.class
            );

            // Si le statut HTTP est un succès (200 OK ou 201 Created)
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("[FRONTEND] Inscription réussie côté user-service !");
                
                // Redirection vers la page de login après succès
                return "redirect:/auth/login?success=account_created";
            }

        } catch (Exception e) {
            System.err.println("[FRONTEND] ERREUR pendant l'inscription : " + e.getMessage());
            e.printStackTrace();
            
            // On renvoie l'erreur à afficher sur le HTML
            model.addAttribute("error", "Erreur lors de l'inscription. L'username ou l'email est peut-être déjà utilisé.");
            return "auth/register"; // Réaffiche la page avec l'erreur
        }

        model.addAttribute("error", "Une erreur inattendue est survenue.");
        return "auth/register";
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

    /**
     * Affiche l'espace personnel du Coach
     * URL d'accès : http://localhost/coach/home
     */
    @GetMapping("/coach/home")
    public String coachHome(HttpSession session, Model model) {
        System.out.println("\n--- [FRONTEND] Agrégation des données pour /coach/home ---");
        
        String role = (String) session.getAttribute("role");
        UUID roleID = (UUID) session.getAttribute("roleID");

        System.out.println("  -> Attribut Session 'role'   : " + role);
        System.out.println("  -> Attribut Session 'roleID' : " + roleID);

        if (!"COACH".equals(role) || roleID == null) {
            System.out.println("[FRONTEND] ÉCHEC SÉCURITÉ COACH: Redirection vers /auth/login");
            return "redirect:/auth/login";
        }
        
        // 1. Récupérer le profil du Coach de base
        try {
            String coachServiceUrl = "http://user-service:8080/api/user/coaches/" + roleID;
            System.out.println("[FRONTEND] Requête vers user-service: GET " + coachServiceUrl);
            ResponseEntity<CoachResponseDTO> response = restTemplate.getForEntity(
                coachServiceUrl, 
                CoachResponseDTO.class
            );
            
            CoachResponseDTO trueCoach = response.getBody();
            System.out.println("[FRONTEND] Coach récupéré avec succès : " + (trueCoach != null ? trueCoach.getName() : "NULL"));

            model.addAttribute("coach", trueCoach);
        } catch (Exception e) {
            System.err.println("[FRONTEND] Erreur profil coach : " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("coach", new CoachResponseDTO(roleID, "Introuvable", "Erreur", ""));
        }

        // 2. COMPOSITION DES COURS (LESSONS)
        java.util.List<Map<String, Object>> lessonsWithData = new java.util.ArrayList<>();
        try {
            // Étape A : Demander au user-service la liste des IDs de leçons de ce coach
            String coachLessonsIdsUrl = "http://user-service:8080/api/user/coaches/" + roleID + "/lessons";
            java.util.List<String> lessonIds = restTemplate.getForObject(coachLessonsIdsUrl, java.util.List.class);
            
            if (lessonIds != null) {
                // Étape B : Pour chaque ID, aller chercher les détails dans le catalog-service
                for (String id : lessonIds) {
                    try {
                        String catalogUrl = "http://catalog-service:8080/api/catalog/lessons/" + id;
                        Map<String, Object> lessonData = restTemplate.getForObject(catalogUrl, Map.class);
                        if (lessonData != null) {
                            lessonsWithData.add(lessonData);
                        }
                    } catch (Exception e) {
                        System.err.println("[FRONTEND] Impossible de récupérer la leçon " + id + " depuis le Catalogue.");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[FRONTEND] Erreur récupération des IDs de leçons : " + e.getMessage());
        }
        model.addAttribute("myLessons", lessonsWithData);


        // 3. COMPOSITION DES ABONNEMENTS (SUBSCRIPTIONS)
        java.util.List<Map<String, Object>> subscriptionsWithData = new java.util.ArrayList<>();
        try {
            // Étape A : Demander au user-service la liste des IDs d'abonnements de ce coach
            String coachSubsIdsUrl = "http://user-service:8080/api/user/coaches/" + roleID + "/subscriptions";
            java.util.List<String> subIds = restTemplate.getForObject(coachSubsIdsUrl, java.util.List.class);
            
            if (subIds != null) {
                // Étape B : Pour chaque ID, aller chercher les détails dans le booking-service
                for (String id : subIds) {
                    try {
                        String bookingUrl = "http://booking-service:8080/api/booking/subscriptions/" + id;
                        Map<String, Object> subData = restTemplate.getForObject(bookingUrl, Map.class);
                        
                        if (subData != null) {
                            // Récupération de la liste brute des IDs de planning stockée dans la réponse de Booking
                            java.util.List<String> rawPlanningIds = (java.util.List<String>) subData.get("planningsID");
                            
                            // Liste finale qui contiendra les objets plannings complets depuis le catalogue
                            java.util.List<Map<String, Object>> fullPlanningsList = new java.util.ArrayList<>();
                            String lessonIdFromPlannings = null;

                            if (rawPlanningIds != null && !rawPlanningIds.isEmpty()) {
                                // Étape C : Pour chaque ID de planning, on interroge le catalog-service
                                for (String planningId : rawPlanningIds) {
                                    try {
                                        String planningCatalogUrl = "http://catalog-service:8080/api/catalog/plannings/" + planningId;
                                        Map<String, Object> planningData = restTemplate.getForObject(planningCatalogUrl, Map.class);
                                        
                                        if (planningData != null) {
                                            fullPlanningsList.add(planningData);
                                            
                                            // On capture le lessonId du premier planning valide qu'on croise
                                            if (lessonIdFromPlannings == null && planningData.get("lessonID") != null) {
                                                lessonIdFromPlannings = (String) planningData.get("lessonID");
                                                System.err.println("[FRONTEND] Lesson trouvée, id : " + lessonIdFromPlannings);
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.err.println("[FRONTEND] Impossible de récupérer les détails du planning " + planningId);
                                    }
                                }
                            }
                            else {
                                if (rawPlanningIds != null) {
                                    System.out.println("[FRONTEND] Liste planning vide");
                                }
                                System.out.println("[FRONTEND] Liste planning nulle");
                            }

                            // On remplace la liste d'IDs de l'abonnement par notre liste d'objets complets
                            subData.put("planningsID", fullPlanningsList);

                            // Étape D : Si on a récupéré un lessonId valide depuis les plannings, on va chercher le nom de la Lesson
                            if (lessonIdFromPlannings != null) {
                                try {
                                    String lessonUrl = "http://catalog-service:8080/api/catalog/lessons/" + lessonIdFromPlannings;
                                    Map<String, Object> lessonData = restTemplate.getForObject(lessonUrl, Map.class);
                                    if (lessonData != null && lessonData.get("name") != null) {
                                        subData.put("lessonName", lessonData.get("name"));
                                    } else {
                                        subData.put("lessonName", "Cours inconnu");
                                    }
                                } catch (Exception e) {
                                    subData.put("lessonName", "Cours indisponible");
                                }
                            } else {
                                subData.put("lessonName", "Aucun cours associé");
                            }

                            subscriptionsWithData.add(subData);
                        }
                    } catch (Exception e) {
                        System.err.println("[FRONTEND] Impossible de récupérer l'abonnement " + id + " depuis Booking.");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[FRONTEND] Erreur globale lors de l'agrégation des abonnements : " + e.getMessage());
        }
        model.addAttribute("mySubscriptions", subscriptionsWithData);

        return "coach/home"; 
    }

    /**
     * Affiche le formulaire de création d'un nouveau type de cours (Catalog)
     * URL d'accès : http://localhost/coach/lesson/new
     */
    @GetMapping("/coach/lesson/new")
    public String showCreateLessonForm(HttpSession session, Model model) {
        System.out.println("\n--- [FRONTEND] Demande formulaire création cours ---");
        
        // Sécurité Coach
        String role = (String) session.getAttribute("role");
        if (!"COACH".equals(role)) {
            return "redirect:/auth/login";
        }

        // On peut passer un objet vide pour l'associer au formulaire Thymeleaf si besoin
        // model.addAttribute("lessonForm", new LessonDTO());
        
        return "coach/create-lesson"; // Cherchera templates/coach/create-lesson.html
    }

    // URL pour créer le cours dans le catalogue
    private final String CATALOG_LESSON_URL = "http://catalog-service:8080/api/catalog/lessons";
    
    // URL pour lier la leçon au coach dans le service utilisateur
    private final String USER_COACH_LESSON_BASE_URL = "http://user-service:8080/api/user/coaches/";

    /**
     * Traite la soumission du formulaire de création de cours
     */
    @PostMapping("/coach/lesson")
    public String handleCreateLesson(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("duration") float duration,
            HttpSession session,
            Model model) {

        System.out.println("\n--- [FRONTEND] Début de création d'un type de cours ---");
        
        // 1. Vérification de la sécurité de la session
        String role = (String) session.getAttribute("role");
        UUID coachId = (UUID) session.getAttribute("roleID");

        if (!"COACH".equals(role) || coachId == null) {
            System.out.println("[FRONTEND] ÉCHEC SÉCURITÉ: Utilisateur non connecté ou n'est pas un Coach.");
            return "redirect:/auth/login";
        }

        // 2. Construction du DTO attendu par le catalogue
        LessonDTO lessonDto = new LessonDTO();
        lessonDto.setName(name);
        lessonDto.setDescription(description);
        lessonDto.setDuration(duration);
        lessonDto.setType("null"); // Fixé à null comme demandé
        lessonDto.setCoachID(coachId);

        try {
            // ÉTAPE 1 : Envoi au catalog-service (POST)
            System.out.println("[FRONTEND] Étape 1 : Envoi de la leçon au catalog-service...");
            ResponseEntity<Map> catalogResponse = restTemplate.postForEntity(
                CATALOG_LESSON_URL, 
                lessonDto, 
                Map.class
            );

            Map<String, Object> createdLesson = catalogResponse.getBody();
            
            if (createdLesson == null || !createdLesson.containsKey("id")) {
                throw new RuntimeException("Le catalogue n'a pas renvoyé d'ID valide pour le cours créé.");
            }

            // Extraction de l'ID de la leçon depuis la réponse (géré proprement sous forme de chaîne transposable en UUID)
            Object idValue = createdLesson.get("id");
            if (idValue == null) {
                throw new RuntimeException("Clé 'id' manquante dans la réponse du catalogue. Clés disponibles : " + createdLesson.keySet());
            }
            String lessonIdStr = idValue.toString();
            System.out.println("[FRONTEND] Récupération de lesson d'id : " + lessonIdStr);
            UUID lessonId = UUID.fromString(lessonIdStr);
            System.out.println("[FRONTEND] -> Cours créé avec succès dans le Catalogue. ID généré : " + lessonId);

            // ÉTAPE 2 : Association au profil Coach (Appel synchrone Inter-service temporaire avant Kafka)
            String linkUrl = USER_COACH_LESSON_BASE_URL + coachId + "/lessons/" + lessonId;
            System.out.println("[FRONTEND] Étape 2 : Liaison de la leçon au coach via user-service...");
            System.out.println("            POST " + linkUrl);

            // Le endpoint renvoie un statut 200 OK sans corps de réponse
            restTemplate.postForEntity(linkUrl, null, Void.class);
            System.out.println("[FRONTEND] -> Liaison effectuée avec succès !");

            // Redirection vers le dashboard après la réussite des deux étapes
            return "redirect:/coach/home";

        } catch (Exception e) {
            System.err.println("[FRONTEND] ERREUR critique pendant le cycle de création du cours : " + e.getMessage());
            e.printStackTrace();
            
            model.addAttribute("error", "Échec de l'enregistrement du cours. Les microservices associés sont peut-être indisponibles.");
            // On réinjecte les jours de la semaine pour que l'affichage ne plante pas si l'utilisateur y revient
            model.addAttribute("daysOfWeek", java.util.List.of("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"));
            return "coach/create-lesson";
        }
    }

    /**
     * Affiche le formulaire de planification d'un abonnement avec créneaux (Booking)
     * URL d'accès : http://localhost/coach/subscription/new
     */
    @GetMapping("/coach/subscription/new")
    public String showCreateSubscriptionForm(HttpSession session, Model model) {
        System.out.println("\n--- [FRONTEND] Agrégation des leçons pour le formulaire d'abonnement ---");
        
        // 1. Sécurité Coach
        String role = (String) session.getAttribute("role");
        UUID roleID = (UUID) session.getAttribute("roleID");
        if (!"COACH".equals(role) || roleID == null) {
            return "redirect:/auth/login";
        }

        // 2. Récupération et résolution des leçons du coach (Composition)
        java.util.List<Map<String, Object>> coachLessonsWithData = new java.util.ArrayList<>();
        try {
            // Étape A : Récupérer les IDs de leçons appartenant au coach depuis le user-service
            String userCoachLessonsUrl = "http://user-service:8080/api/user/coaches/" + roleID + "/lessons";
            System.out.println("[FRONTEND] Récupération des IDs de leçons : GET " + userCoachLessonsUrl);
            java.util.List<String> lessonIds = restTemplate.getForObject(userCoachLessonsUrl, java.util.List.class);
            
            if (lessonIds != null && !lessonIds.isEmpty()) {
                // Étape B : Pour chaque ID, interroger le catalogue pour avoir le nom et les détails
                for (String id : lessonIds) {
                    try {
                        String catalogUrl = "http://catalog-service:8080/api/catalog/lessons/" + id;
                        Map<String, Object> lessonData = restTemplate.getForObject(catalogUrl, Map.class);
                        if (lessonData != null) {
                            coachLessonsWithData.add(lessonData);
                        }
                    } catch (Exception e) {
                        System.err.println("[FRONTEND] Impossible de récupérer les détails du cours " + id + " via le catalogue.");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[FRONTEND] Erreur lors de la récupération de la liste des cours du coach : " + e.getMessage());
        }

        // 3. Injection des données dans le modèle
        // Si la liste est vide (aucun cours créé ou services hors-ligne), l'interface le gérera proprement
        model.addAttribute("myLessons", coachLessonsWithData);
        
        // Liste des jours de la semaine pour construire dynamiquement l'interface des plannings
        model.addAttribute("daysOfWeek", java.util.List.of("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"));

        return "coach/create-subscription"; // Oriente vers templates/coach/create-subscription.html
    }

    private LocalDateTime parseFormToLocalDateTime(String weekStr, String dayFrench, String timeStr) {
        // 1. Extraire l'année et le numéro de semaine depuis "2026-W27"
        String[] weekParts = weekStr.split("-W");
        int year = Integer.parseInt(weekParts[0]);
        int weekNumber = Integer.parseInt(weekParts[1]);

        // 2. Traduire le jour français en énumération DayOfWeek standard
        DayOfWeek dayOfWeek;
        switch (dayFrench.toLowerCase()) {
            case "lundi":    dayOfWeek = DayOfWeek.MONDAY; break;
            case "mardi":    dayOfWeek = DayOfWeek.TUESDAY; break;
            case "mercredi": dayOfWeek = DayOfWeek.WEDNESDAY; break;
            case "jeudi":    dayOfWeek = DayOfWeek.THURSDAY; break;
            case "vendredi": dayOfWeek = DayOfWeek.FRIDAY; break;
            case "samedi":   dayOfWeek = DayOfWeek.SATURDAY; break;
            case "dimanche": dayOfWeek = DayOfWeek.SUNDAY; break;
            default: throw new IllegalArgumentException("Jour inconnu : " + dayFrench);
        }

        // 3. Obtenir le premier jour de cette semaine de l'année
        // Utilisation de la définition de semaine ISO (la semaine commence le lundi)
        LocalDateTime targetDate = LocalDateTime.of(year, 1, 1, 0, 0)
                .with(WeekFields.of(Locale.FRANCE).weekOfYear(), weekNumber)
                .with(TemporalAdjusters.previousOrSame(dayOfWeek));

        // 4. Associer l'heure choisie (ex: "08:30")
        LocalTime time = LocalTime.parse(timeStr);
        
        return targetDate.with(time);
    }

    @PostMapping("/coach/subscription")
    public String handleCreateSubscription(
            @RequestParam("lessonId") String lessonId,
            @RequestParam("offerName") String offerName,
            @RequestParam("price") float price,
            @RequestParam("capacity") int capacity,
            @RequestParam("targetWeek") String weekStr,
            @RequestParam(value = "formattedSchedules", required = false) List<String> formattedSchedules,
            HttpSession session,
            Model model) {

        System.out.println("\n--- [FRONTEND] Début de l'orchestration (Plannings par Date Spécifique 2026) ---");

        String role = (String) session.getAttribute("role");
        UUID roleID = (UUID) session.getAttribute("roleID");

        if (!"COACH".equals(role) || roleID == null) {
            return "redirect:/auth/login";
        }

        if (formattedSchedules == null || formattedSchedules.isEmpty()) {
            return "redirect:/coach/subscription/new?error=missing_schedules";
        }

        List<UUID> createdPlanningIds = new java.util.ArrayList<>();

        try {
            // =========================================================================
            // ÉTAPE 1 : Conversion temporelle et envoi au catalog-service
            // =========================================================================
            String catalogServiceUrl = "http://catalog-service:8080/api/catalog/plannings";
            UUID lessonUuid = UUID.fromString(lessonId);

            for (String scheduleStr : formattedSchedules) {
                System.out.println("\n--- [FRONTEND] Listing des plannings : " + scheduleStr);
                // Découpe la chaîne "2026-W27@Lundi@08:30"
                String[] parts = scheduleStr.split("@");
                if (parts.length != 2) return "redirect:/coach/subscription/new?error=wrong_planning";

                String dayFrench = parts[0]; // "Lundi"
                String timeStr = parts[1];   // "08:30"

                // Calcul du LocalDateTime précis
                LocalDateTime finalDateTime = parseFormToLocalDateTime(weekStr, dayFrench, timeStr);
                
                // Conversion au format ISO-8601 pour l'envoi JSON
                String isoDateTimeStr = finalDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                PlanningDTO planningPayload = new PlanningDTO();
                planningPayload.setDateTime(finalDateTime);
                planningPayload.setDuration(10);
                planningPayload.setLessonID(lessonUuid);
                
                System.out.println("[FRONTEND] Envoi catalog-service -> Date calculée : " + isoDateTimeStr);

                Map<String, Object> catalogResponse = restTemplate.postForObject(catalogServiceUrl, planningPayload, Map.class);

                System.out.println("\n--- [FRONTEND] Response à l'ajout de planning : " + catalogResponse.toString());

                if (catalogResponse != null && catalogResponse.containsKey("id")) {
                    String responseId = catalogResponse.get("id").toString();
                    UUID UUIDResponseId = UUID.fromString(responseId);
                    createdPlanningIds.add(UUIDResponseId);
                } else {
                    throw new RuntimeException("catalog-service n'a pas renvoyé d'id valide.");
                }
            }

            if (createdPlanningIds.isEmpty()) {
                throw new RuntimeException("Aucun planning n'a pu être persisté dans le catalogue.");
            }

            // =========================================================================
            // ÉTAPE 2 : Création de l'Abonnement (Booking-service)
            // =========================================================================
            String bookingServiceUrl = "http://booking-service:8080/api/booking/subscriptions";
            LocalDate endDate = LocalDate.of(2026, 12, 31);
            SubscriptionDTO subscriptionPayload = new SubscriptionDTO();
            subscriptionPayload.setName(offerName);
            subscriptionPayload.setPrice(price);
            subscriptionPayload.setCapacity(capacity);
            subscriptionPayload.setEndDate(endDate);
            subscriptionPayload.setPlanningsID(createdPlanningIds);

            System.out.println("[FRONTEND] Envoi booking-service : POST " + bookingServiceUrl);
            Map<String, Object> bookingResponse = restTemplate.postForObject(bookingServiceUrl, subscriptionPayload, Map.class);

            UUID subscriptionId = null;
            if (bookingResponse != null && bookingResponse.containsKey("id")) {
                String responseId = bookingResponse.get("id").toString();
                subscriptionId = UUID.fromString(responseId);
            } else {
                throw new RuntimeException("booking-service n'a pas renvoyé d'id d'abonnement.");
            }

            // =========================================================================
            // ÉTAPE 3 : Liaison de la Subscription au Coach (User-service)
            // =========================================================================
            String userServiceUrl = "http://user-service:8080/api/user/coaches/" + roleID + "/subscriptions/" + subscriptionId;
            System.out.println("[FRONTEND] Liaison Coach-Abonnement via user-service : POST " + userServiceUrl);

            restTemplate.postForObject(userServiceUrl, null, String.class);
            System.out.println("[FRONTEND] Pipeline d'orchestration temporelle complété !");

        } catch (Exception e) {
            System.err.println("[FRONTEND] ERREUR CRITIQUE pendant l'orchestration temporelle : " + e.getMessage());
            e.printStackTrace();
            return "redirect:/coach/subscription/new?error=orchestration_failed";
        }

        return "redirect:/coach/home";
        //return "redirect:/coach/home?success=subscription_created";
    }

    
    /**
     * Traite la déconnexion de l'utilisateur
     * URL d'accès : /auth/logout
     */
    @GetMapping("/auth/logout")
    public String handleLogout(HttpSession session) {
        System.out.println("\n--- [FRONTEND] Déconnexion demandée ---");
        
        String username = (String) session.getAttribute("username");
        if (username != null) {
            System.out.println("[FRONTEND] Fermeture de la session pour l'utilisateur : " + username);
        }

        // Détruit complètement la session actuelle et nettoie ses attributs
        session.invalidate();

        // Redirige l'utilisateur vers la page d'accueil (index.html)
        return "redirect:/";
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

@Data
@AllArgsConstructor
@NoArgsConstructor
class CoachResponseDTO {
    private UUID id;
    private String surname;
    private String name;
    private String email;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class RegistrationDTO {
    private String username;
    private String password;
    private String role;
    private String surname;
    private String name;
    private String email;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class LessonDTO {
    private String name;
    private String description;
    private float duration;
    private String type;
    private UUID coachID;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SubscriptionDTO {
    private String name;
    private float price;
    private Integer capacity;
    private LocalDate endDate;
    private List<UUID> planningsID;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PlanningDTO {
    private LocalDateTime dateTime;
    private float Duration;
    private UUID lessonID;
}