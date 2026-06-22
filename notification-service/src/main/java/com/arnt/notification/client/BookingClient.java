package com.arnt.notification.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BookingClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("$(booking.service.url)")
    private String bookingServiceUrl;

    /**
     * Make a REST request to get the Subscription name.
     * 
     * @param subscriptionID the Subscription id
     * @return the Subscription name
     * @throws IOException
     * @throws InterruptedException
     */
    public String getSubscriptionName(UUID subscriptionID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(bookingServiceUrl + "/api/booking/subscriptions/" + subscriptionID))
            .GET()
            .build();
    
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response.body());
        return json.get("name").toString();
    }
}
