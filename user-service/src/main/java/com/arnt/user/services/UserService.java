package com.arnt.user.services;

import com.arnt.user.dto.LoginRequestDTO;
import com.arnt.user.dto.LoginResponseDTO;
import com.arnt.user.dto.RegistrationDTO;
import com.arnt.user.entities.User;

public interface UserService {
    
    /**
     * Enregistre un nouvel utilisateur (User + Profil spécifique).
     */
    User registerNewUser(RegistrationDTO dto);

    /**
     * Authentifie un utilisateur et retourne ses informations avec un token JWT.
     */
    LoginResponseDTO login(LoginRequestDTO dto);

    /**
     * Déconnexion (Optionnel - pour matcher ton controller).
     */
    void logout();

    /**
     * Rafraîchir le token (Optionnel - pour matcher ton controller).
     */
    LoginResponseDTO refresh();
}