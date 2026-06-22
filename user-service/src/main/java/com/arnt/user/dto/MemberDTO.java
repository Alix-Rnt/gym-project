package com.arnt.user.dto;

import java.util.UUID;
import java.util.List;

import com.arnt.user.entities.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data Transfer Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String surname;
    private String name;
    private String email;
    private List<UUID> subscriptionList;
    private List<UUID> waitlistList;
    private UUID userID;

    /**
     * Convert DTO to Member
     * 
     * @return Member entity
     */

    public Member toEntity() {
        Member member = new Member();
        member.setSurname(this.surname);
        member.setName(this.name);
        member.setEmail(this.email);
        member.setSubscriptionList(this.subscriptionList);
        member.setWaitlistList(this.waitlistList);
        member.setUserID(this.userID);
        return member;
    }
}
