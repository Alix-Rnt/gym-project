package com.arnt.user.dto;

import com.arnt.user.entities.User;
import com.arnt.user.entities.Member;
import com.arnt.user.entities.Coach;
import com.arnt.user.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Registration Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    private String username;
    private String password;
    private UserRole role;
    private String surname;
    private String name;
    private String email;

    /**
     * Convert DTO to User
     * 
     * @return User entity
     */

    public User toUserEntity() {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setRole(this.role);
        return user;
    }

    /**
     * Convert DTO to Member
     * 
     * @return Member entity
     */

    public Member toMemberEntity() {
        Member member = new Member();
        member.setSurname(this.surname);
        member.setName(this.name);
        member.setEmail(this.email);
        return member;
    }

    /**
     * Convert DTO to Coach
     * 
     * @return Coach entity
     */

    public Coach toCoachEntity() {
        Coach coach = new Coach();
        coach.setSurname(this.surname);
        coach.setName(this.name);
        coach.setEmail(this.email);
        return coach;
    }
}
