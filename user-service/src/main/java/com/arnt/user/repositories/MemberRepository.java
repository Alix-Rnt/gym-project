package com.arnt.user.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import com.arnt.user.entities.Member;

/**
 * MemberRepository holds all the members.
 */
@Repository
public class MemberRepository {
    /** Member list. */
    private final List<Member> members = new ArrayList<>();

    /**
     * Adds a Member to the repository.
     * * @param member the said Member to be added
     * @return the added Member
     */
    public Member save(Member member) {
        members.add(member);
        return member;
    }

    /**
     * Get the Member list.
     * * @return the whole repository as a list
     */
    public List<Member> findAll() {
        return members;
    }

    /**
     * Return the first Member with a specific id.
     * * @param id the Member id
     * @return the found Member or empty Optional if not found
     */
    public Optional<Member> findById(UUID id) {
        return members.stream()
                      .filter(e -> e.getId().equals(id))
                      .findFirst();
    }

    /**
     * Return the first Member linked to a specific userID.
     * * @param userID the User id reference
     * @return the found Member or empty Optional if not found
     */
    public Optional<Member> findByUserID(UUID userID) {
        return members.stream()
                      .filter(e -> e.getUserID().equals(userID))
                      .findFirst();
    }

    /**
     * Remove the first Member with a specific id.
     * * @param id the Member id
     */
    public void delete(UUID id) {
        members.removeIf(e -> e.getId().equals(id));
    }

    /**
     * Completely erases the repository.
     */
    public void clear() {
        members.clear();
    }
}