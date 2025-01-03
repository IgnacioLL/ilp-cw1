package com.example.ilp_cw1.utils;

import com.example.ilp_cw1.model.Pizza;
import com.example.ilp_cw1.model.Restaurant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;


public class DownloadRestaurants {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private Restaurant[] restaurantsInfo; // Make private

    public DownloadRestaurants(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Restaurant[] getRestaurantsInfo() throws JsonProcessingException, HttpClientErrorException {
        String url = "https://ilp-rest-2024.azurewebsites.net/restaurants";
        String jsonResponse = restTemplate.getForObject(url, String.class);

        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return new Restaurant[0]; // Return empty array if no data
        }

        restaurantsInfo = objectMapper.readValue(jsonResponse, Restaurant[].class);
        return restaurantsInfo;
    }

    public Set<String> getAllPizzaNames() throws JsonProcessingException, HttpClientErrorException {
        if (restaurantsInfo == null) { // Fetch data only if not already fetched
            restaurantsInfo = getRestaurantsInfo();
        }

        Set<String> pizzaNames = new HashSet<>();

        for (Restaurant restaurant : Optional.ofNullable(restaurantsInfo).orElse(new Restaurant[0])) {
            Optional.ofNullable(restaurant)
                    .map(Restaurant::getMenu)
                    .ifPresent(menu -> menu.stream()
                            .map(Pizza::getName)
                            .filter(Objects::nonNull)
                            .forEach(pizzaNames::add));
        }

        return pizzaNames;
    }
    public Map<String, Restaurant> createPizzaRestaurantMap() throws JsonProcessingException, HttpClientErrorException {
        if (restaurantsInfo == null) { // Fetch data only if not already fetched
            restaurantsInfo = getRestaurantsInfo();
        }
        Map<String, Restaurant> pizzaRestaurantMap = new HashMap<>();

        for (Restaurant restaurant : restaurantsInfo) {
            for (Pizza pizza : restaurant.getMenu()) {
                pizzaRestaurantMap.put(pizza.getName(), restaurant);
            }
        }

        return pizzaRestaurantMap;
    }

    public Map<String, Integer> createPizzaPriceMap() throws JsonProcessingException, HttpClientErrorException {
        if (restaurantsInfo == null) { // Fetch data only if not already fetched
            restaurantsInfo = getRestaurantsInfo();
        }
        Map<String, Integer> pizzaPriceMap = new HashMap<>();

        for (Restaurant restaurant : restaurantsInfo) {
            for (Pizza pizza : restaurant.getMenu()) {
                pizzaPriceMap.put(pizza.getName(), pizza.getPriceInPence());
            }
        }

        return pizzaPriceMap;
    }
}