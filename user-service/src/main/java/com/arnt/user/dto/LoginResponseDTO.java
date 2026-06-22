package com.arnt.user.dto;

import com.arnt.user.entities.User;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String username;
    private String token;
    private String role;
    private UUID roleID;

    /**
     * Convert DTO to User
     * 
     * @return User entity
     */

    public User toEntity() {
        User user = new User();
        // user.setUsername(this.username);
        // user.setPassword(this.password);
        return user;
    }
}
