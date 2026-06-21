package com.arnt.booking.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.arnt.booking.dto.WaitlistDTO;
import com.arnt.booking.entities.Waitlist;
import com.arnt.booking.exceptions.MemberNotFoundException;
import com.arnt.booking.exceptions.SubscriptionNotFoundException;
import com.arnt.booking.exceptions.WaitlistEmptyException;
import com.arnt.booking.exceptions.WaitlistNotFoundException;
import com.arnt.booking.repositories.WaitlistRepository;

@Service
public class WaitlistServiceImpl implements WaitlistService {
    private final WaitlistRepository waitlistRepository;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("$(user.service.url)")
    private String userServiceUrl;

    public WaitlistServiceImpl(WaitlistRepository waitlistRepository) {
        this.waitlistRepository = waitlistRepository;
    }

    @Override
    public List<Waitlist> getAll() {
        return waitlistRepository.findAll();
    }

    @Override
    public Waitlist get(UUID id) {
        return waitlistRepository
                .findById(id)
                .orElseThrow(() -> new WaitlistNotFoundException(id));
    }

    @Override
    public Waitlist save(WaitlistDTO dto) {
        Waitlist waitlist = dto.toEntity();

        waitlistRepository.save(waitlist);

        return waitlist;
    }

    @Override
    public Waitlist update(UUID id, WaitlistDTO dto) {
        Waitlist waitlist = this.get(id);

        waitlist.setMembersTimestamp(dto.getMembersTimestamp());
        waitlist.setSubscriptionID(dto.getSubscriptionID());

        waitlistRepository.save(waitlist);

        return waitlist;
    }

    @Override
    public void delete(UUID id) {
        this.get(id);
        waitlistRepository.delete(id);
    }

    @Override
    public Integer getMemberAmount(UUID id) {
        Waitlist waitlist = this.get(id);
        return waitlist.getMembersTimestamp().size();
    }

    @Override
    public Waitlist getBySubscriptionId(UUID id) {
        return waitlistRepository
                .findBySubscriptionId(id)
                .orElseThrow(() -> new SubscriptionNotFoundException(id));
    }

    @Override
    public void addMember(UUID id, UUID memberId) throws IOException, InterruptedException {
        Waitlist waitlist = this.get(id);
        validateMember(memberId);

        waitlist.getMembersTimestamp().put(memberId, new Timestamp(System.currentTimeMillis()));
        waitlistRepository.save(waitlist);
    }

    @Override
    public void removeMember(UUID id, UUID memberId) {
        Waitlist waitlist = this.get(id);
        waitlist.getMembersTimestamp().remove(memberId);
        waitlistRepository.save(waitlist);
    }

    @Override
    public void addMemberBySubscriptionId(UUID subscriptionId, UUID memberId) throws IOException, InterruptedException {
        Waitlist waitlist = this.getBySubscriptionId(subscriptionId);
        this.addMember(waitlist.getId(), memberId);
    }

    @Override
    public UUID popMember(UUID id) {
        Waitlist waitlist = this.get(id);
        UUID memberID = waitlist.getMembersTimestamp().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new WaitlistEmptyException(id));

        waitlist.getMembersTimestamp().remove(memberID);
        waitlistRepository.save(waitlist);

        return memberID;
    }

    /**
     * Checks if Member refered in id is valid.
     * 
     * @param id the Member id
     * @throws IOException
     * @throws InterruptedException
     */
    private void validateMember(UUID id) throws IOException, InterruptedException {
        HttpRequest userRequest = HttpRequest.newBuilder()
                .uri(URI.create(userServiceUrl + "/api/user/members/" + id))
                .GET()
                .build();
        if (httpClient.send(userRequest, HttpResponse.BodyHandlers.ofString()).statusCode() == 404) {
            throw new MemberNotFoundException(id);
        }
    }

}
