package com.example.ilp_cw1.utils;

import com.example.ilp_cw1.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class DownloadCentralArea {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public Region centralArea;

    public DownloadCentralArea(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Region getCentralArea(){
        return this.centralArea;
    }

    public void setCentralArea() {
        String url = "https://ilp-rest-2024.azurewebsites.net/centralArea";

        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                this.centralArea = objectMapper.readValue(jsonResponse, Region.class);

            } else {
                // Handle empty response
                System.err.println("Error: Empty or null response from ILP server");
            }

        } catch (HttpClientErrorException e) {
            System.err.println("ILP Server Error: " + e.getStatusCode() + " - " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error fetching restaurants: " + e.getMessage());
        }
    }
}