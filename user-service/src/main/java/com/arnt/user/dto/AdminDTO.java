package com.arnt.user.dto;

import java.util.UUID;

import com.arnt.user.entities.Admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private UUID userID;

    /**
     * Convert DTO to Admin
     * 
     * @return Admin entity
     */

    public Admin toEntity() {
        Admin admin = new Admin();
        admin.setUserID(this.userID);
        return admin;
    }
}
