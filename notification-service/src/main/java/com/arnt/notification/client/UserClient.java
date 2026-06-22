package com.arnt.notification.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("$(user.service.url)")
    private String bookingServiceUrl;

    /**
     * Make a REST request to get the Member email.
     * 
     * @param memberID the Member id
     * @return the Member email
     * @throws IOException
     * @throws InterruptedException
     */
    public List<String> getMemberEmails(List<UUID> memberIDs) throws IOException, InterruptedException {
        List<String> emails = new ArrayList<>();
        
        for (UUID memberID : memberIDs) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(bookingServiceUrl + "/api/user/members/" + memberID))
                .GET()
                .build();
        
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            emails.add(json.get("email").toString());
        }

        return emails;
    }


}
