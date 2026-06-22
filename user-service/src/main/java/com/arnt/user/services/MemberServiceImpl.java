package com.arnt.user.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.arnt.user.dto.MemberDTO;
import com.arnt.user.entities.Member;
import com.arnt.user.repositories.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Collection<Member> getAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member get(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));
    }

    @Override
    public Member save(MemberDTO dto) {
        Member member = dto.toEntity();
        
        // Si l'ID n'est pas déjà injecté (par exemple par le UserService lors de l'inscription)
        if (member.getId() == null) {
            member.setId(UUID.randomUUID());
        }
        
        // Initialisation des listes d'UUID vides pour éviter les NullPointerException
        member.setSubscriptionList(new ArrayList<UUID>());
        member.setWaitlistList(new ArrayList<UUID>());
        
        return memberRepository.save(member);
    }

    @Override
    public Member update(UUID id, MemberDTO dto) {
        Member existingMember = get(id);
        existingMember.setSurname(dto.getSurname());
        existingMember.setName(dto.getName());
        existingMember.setEmail(dto.getEmail());
        return existingMember;
    }

    @Override
    public void delete(UUID id) {
        memberRepository.delete(id);
    }

    // =========================================================================
    // REFERENCES : SUBSCRIPTIONS (Lien logique avec le Microservice Booking)
    // =========================================================================

    @Override
    public Collection<UUID> getMemberSubscriptions(UUID memberId) {
        return get(memberId).getSubscriptionList();
    }

    @Override
    public void addSubscription(UUID memberId, UUID subscriptionId) {
        Member member = get(memberId);
        if (!member.getSubscriptionList().contains(subscriptionId)) {
            member.getSubscriptionList().add(subscriptionId);
        }
    }

    @Override
    public void removeSubscription(UUID memberId, UUID subscriptionId) {
        Member member = get(memberId);
        member.getSubscriptionList().remove(subscriptionId);
    }

    // =========================================================================
    // REFERENCES : WAITLISTS (Lien logique avec le Microservice Booking)
    // =========================================================================

    @Override
    public Collection<UUID> getMemberWaitlists(UUID memberId) {
        return get(memberId).getWaitlistList();
    }

    @Override
    public void addWaitlist(UUID memberId, UUID waitlistId) {
        Member member = get(memberId);
        if (!member.getWaitlistList().contains(waitlistId)) {
            member.getWaitlistList().add(waitlistId);
        }
    }

    @Override
    public void removeWaitlist(UUID memberId, UUID waitlistId) {
        Member member = get(memberId);
        member.getWaitlistList().remove(waitlistId);
    }
}