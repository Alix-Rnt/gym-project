package com.arnt.user.dto;

import com.arnt.user.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
    private String username;
    private String password;

    /**
     * Convert DTO to User
     * 
     * @return User entity
     */

    public User toEntity() {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        return user;
    }
}
