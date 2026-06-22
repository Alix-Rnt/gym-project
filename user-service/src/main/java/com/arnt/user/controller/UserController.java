package com.arnt.user.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arnt.user.dto.LoginRequestDTO;
import com.arnt.user.dto.LoginResponseDTO;
import com.arnt.user.dto.MemberDTO;
import com.arnt.user.dto.RegistrationDTO;
import com.arnt.user.dto.CoachDTO;
import com.arnt.user.entities.User;
import com.arnt.user.entities.Member;
import com.arnt.user.entities.Coach;
import com.arnt.user.services.UserService;
import com.arnt.user.services.MemberService;
import com.arnt.user.services.CoachService;
import com.arnt.user.services.AdminService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MemberService memberService;
    private final CoachService coachService;
    private final AdminService adminService;

    /**
     * UserController constructor.
     */
    public UserController(UserService userService, MemberService memberService, 
                          CoachService coachService, AdminService adminService) {
        this.userService = userService;
        this.memberService = memberService;
        this.coachService = coachService;
        this.adminService = adminService;
    }

    /*
        =====================
        Authentication Requests
        =====================
    */

    /**
     * Register a new user (Member or Coach).
     * POST /auth/register
     */
    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDTO register(@RequestBody RegistrationDTO dto) throws IOException, InterruptedException {
        // 1. On crée l'utilisateur et son profil spécifique
        User user = userService.registerNewUser(dto);
        
        // 2. On bascule sur le login automatique
        LoginRequestDTO loginDTO = new LoginRequestDTO(user.getUsername(), dto.getPassword());
        return userService.login(loginDTO);
    }

    /**
     * User login authentication.
     * POST /auth/login
     */
    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
        return userService.login(dto);
    }

    /**
     * User logout.
     * POST /auth/logout
     */
    @PostMapping("/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        userService.logout();
    }

    /**
     * Refresh connection token.
     * POST /auth/refresh
     */
    @PostMapping("/auth/refresh")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO refreshToken() {
        return userService.refresh();
    }

    /*
        =====================
        Member Profile Requests
        =====================
    */

    /**
     * List all members.
     * GET /members
     */
    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Member> getMembers() {
        return memberService.getAll();
    }

    /**
     * Get one member profile.
     * GET /members/{id}
     */
    @GetMapping("/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Member getMember(@PathVariable UUID id) {
        return memberService.get(id);
    }

    /**
     * Update one member profile.
     * PUT /members/{id}
     */
    @PutMapping("/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Member updateMember(@PathVariable UUID id, @RequestBody MemberDTO dto) throws IOException, InterruptedException {
        return memberService.update(id, dto);
    }

    /**
     * Delete one member profile.
     * DELETE /members/{id}
     */
    @DeleteMapping("/members/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable UUID id) {
        memberService.delete(id);
    }

    /*
        =====================
        Member References (Subscriptions & Waitlists)
        =====================
    */

    /**
     * Get all subscription IDs from a member.
     * GET /members/{id}/subscriptions
     */
    @GetMapping("/members/{id}/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UUID> getMemberSubscriptions(@PathVariable UUID id) {
        return memberService.getMemberSubscriptions(id);
    }

    /**
     * Link a subscription ID to a member profile. (Appel Inter-service)
     * POST /members/{id}/subscriptions/{subscriptionId}
     */
    @PostMapping("/members/{id}/subscriptions/{subscriptionId}")
    @ResponseStatus(HttpStatus.OK)
    public void addSubscriptionToMember(@PathVariable UUID id, @PathVariable UUID subscriptionId) {
        memberService.addSubscription(id, subscriptionId);
    }

    /**
     * Unlink a subscription ID from a member profile. (Appel Inter-service)
     * DELETE /members/{id}/subscriptions/{subscriptionId}
     */
    @DeleteMapping("/members/{id}/subscriptions/{subscriptionId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeSubscriptionFromMember(@PathVariable UUID id, @PathVariable UUID subscriptionId) {
        memberService.removeSubscription(id, subscriptionId);
    }

    /**
     * Get all waitlist IDs from a member.
     * GET /members/{id}/waitlists
     */
    @GetMapping("/members/{id}/waitlists")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UUID> getMemberWaitlists(@PathVariable UUID id) {
        return memberService.getMemberWaitlists(id);
    }

    /**
     * Link a waitlist ID to a member profile. (Appel Inter-service)
     * POST /members/{id}/waitlists/{waitlistId}
     */
    @PostMapping("/members/{id}/waitlists/{waitlistId}")
    @ResponseStatus(HttpStatus.OK)
    public void addWaitlistToMember(@PathVariable UUID id, @PathVariable UUID waitlistId) {
        memberService.addWaitlist(id, waitlistId);
    }

    /**
     * Unlink a waitlist ID from a member profile. (Appel Inter-service)
     * DELETE /members/{id}/waitlists/{waitlistId}
     */
    @DeleteMapping("/members/{id}/waitlists/{waitlistId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeWaitlistFromMember(@PathVariable UUID id, @PathVariable UUID waitlistId) {
        memberService.removeWaitlist(id, waitlistId);
    }

    /*
        =====================
        Coach Profile Requests
        =====================
    */

    /**
     * List all coaches.
     * GET /coaches
     */
    @GetMapping("/coaches")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Coach> getCoaches() {
        return coachService.getAll();
    }

    /**
     * Get one coach profile.
     * GET /coaches/{id}
     */
    @GetMapping("/coaches/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Coach getCoach(@PathVariable UUID id) {
        return coachService.get(id);
    }

    /**
     * Create a new coach profile manually (Hors route d'inscription classique).
     * POST /coaches
     */
    @PostMapping("/coaches")
    @ResponseStatus(HttpStatus.CREATED)
    public Coach createCoach(@RequestBody CoachDTO dto) throws IOException, InterruptedException {
        return coachService.save(dto);
    }

    /**
     * Update one coach profile.
     * PUT /coaches/{id}
     */
    @PutMapping("/coaches/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Coach updateCoach(@PathVariable UUID id, @RequestBody CoachDTO dto) throws IOException, InterruptedException {
        return coachService.update(id, dto);
    }

    /**
     * Delete one coach profile.
     * DELETE /coaches/{id}
     */
    @DeleteMapping("/coaches/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoach(@PathVariable UUID id) {
        coachService.delete(id);
    }

    /*
        =====================
        Coach References (Lessons & Subscriptions)
        =====================
    */

    /**
     * Get all lesson IDs created by a coach.
     * GET /coaches/{id}/lessons
     */
    @GetMapping("/coaches/{id}/lessons")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UUID> getCoachLessons(@PathVariable UUID id) {
        return coachService.getCoachLessons(id);
    }

    /**
     * Link a lesson ID to a coach profile. (Appel Inter-service)
     * POST /coaches/{id}/lessons/{lessonId}
     */
    @PostMapping("/coaches/{id}/lessons/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLessonToCoach(@PathVariable UUID id, @PathVariable UUID lessonId) {
        coachService.addLesson(id, lessonId);
    }

    /**
     * Unlink a lesson ID from a coach profile. (Appel Inter-service)
     * DELETE /coaches/{id}/lessons/{lessonId}
     */
    @DeleteMapping("/coaches/{id}/lessons/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLessonFromCoach(@PathVariable UUID id, @PathVariable UUID lessonId) {
        coachService.removeLesson(id, lessonId);
    }

    /**
     * Get all subscription IDs managed by a coach.
     * GET /coaches/{id}/subscriptions
     */
    @GetMapping("/coaches/{id}/subscriptions")
    @ResponseStatus(HttpStatus.OK)
    public Collection<UUID> getCoachSubscriptions(@PathVariable UUID id) {
        return coachService.getCoachSubscriptions(id);
    }

    /**
     * Link a subscription ID to a coach profile. (Appel Inter-service)
     * POST /coaches/{id}/subscriptions/{subscriptionId}
     */
    @PostMapping("/coaches/{id}/subscriptions/{subscriptionId}")
    @ResponseStatus(HttpStatus.OK)
    public void addSubscriptionToCoach(@PathVariable UUID id, @PathVariable UUID subscriptionId) {
        coachService.addSubscription(id, subscriptionId);
    }

    /**
     * Unlink a subscription ID from a coach profile. (Appel Inter-service)
     * DELETE /coaches/{id}/subscriptions/{subscriptionId}
     */
    @DeleteMapping("/coaches/{id}/subscriptions/{subscriptionId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeSubscriptionFromCoach(@PathVariable UUID id, @PathVariable UUID subscriptionId) {
        coachService.removeSubscription(id, subscriptionId);
    }
}