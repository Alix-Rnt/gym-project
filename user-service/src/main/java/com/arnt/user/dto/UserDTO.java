package com.arnt.user.dto;

import java.util.UUID;

import com.arnt.user.entities.User;
import com.arnt.user.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String password;
    private UserRole role;
    private UUID roleID;

    /**
     * Convert DTO to User
     * 
     * @return User entity
     */

    public User toEntity() {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setRole(this.role);
        user.setRoleID(this.roleID);
        return user;
    }
}
