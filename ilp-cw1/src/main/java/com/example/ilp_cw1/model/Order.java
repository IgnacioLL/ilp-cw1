package com.example.ilp_cw1.model;

import com.example.ilp_cw1.utils.DownloadRestaurants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

public class Order {
    private String orderNo;
    private String orderDate;
    private int priceTotalInPence;
    private List<Pizza> pizzasInOrder;
    private CreditCard creditCardInformation;

    public OrderStatus orderStatus;

    @JsonIgnore
    @Autowired
    private DownloadRestaurants downloadRestaurants;

    public Order() {
    }

    public Order(DownloadRestaurants downloadRestaurants) {
        this.downloadRestaurants = downloadRestaurants;
    }

    // Getters and Setters
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

    public void setPriceTotalInPence(int priceTotalInPence) {
        this.priceTotalInPence = priceTotalInPence;
    }

    public List<Pizza> getPizzasInOrder() {
        return pizzasInOrder;
    }

    public void setPizzasInOrder(List<Pizza> pizzasInOrder) {
        this.pizzasInOrder = pizzasInOrder;
    }

    public CreditCard getCreditCardInformation() {
        return creditCardInformation;
    }

    public void setCreditCardInformation(CreditCard creditCardInformation) {
        this.creditCardInformation = creditCardInformation;
    }

    public OrderStatus checkOrderStatus() {
        try {
            if (!this.validateNotEmptyOrder()) {
                return new OrderStatus("EMPTY_ORDER", "INVALID");
            }

            if (!this.validateCardNumber()) {
                return new OrderStatus("CARD_NUMBER_INVALID", "INVALID");
            }

            if (!this.validateExpirateDateCard()) {
                return new OrderStatus("EXPIRY_DATE_INVALID", "INVALID");
            }

            if (!this.validateCvvValid()) {
                return new OrderStatus("CVV_INVALID", "INVALID");
            }

            if (!this.validateOrderTotal()) {
                return new OrderStatus("TOTAL_INCORRECT", "INVALID");
            }

            if (!this.validatePizzaDefined()) {
                return new OrderStatus("PIZZA_NOT_DEFINED", "INVALID");
            }

            if (!this.validateMaxPizzaCount()) {
                return new OrderStatus("MAX_PIZZA_COUNT_EXCEEDED", "INVALID");
            }

            if (!this.validateSingleRestaurantsOrder()) {
                return new OrderStatus("PIZZA_FROM_MULTIPLE_RESTAURANTS", "INVALID");
            }

            if (!this.validateRestaurantOpen()) {
                return new OrderStatus("RESTAURANT_CLOSED", "INVALID");
            }

            if (!this.validatePricePerPizza()) {
                return new OrderStatus("PRICE_FOR_PIZZA_INVALID", "INVALID");
            }
        } catch (JsonProcessingException | ParseException e) {
            return new OrderStatus("UNDEFINED", "UNDEFINED");
        }
        return new OrderStatus("NO ERROR", "VALID");
    }

    public Boolean validateCardNumber() {
        String creditCard = this.creditCardInformation.getCreditCardNumber();
        return (creditCard.length() == 16) && (isNumeric(creditCard));
    }

    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false; // If any character is not a digit, return false
            }
        }
        return true; // If all characters are digits, return true
    }

    public boolean validateExpirateDateCard() throws ParseException {
        SimpleDateFormat expiryFormatter = new SimpleDateFormat("MM/yy");
        Date expiryDateRaw = expiryFormatter.parse(this.creditCardInformation.getCreditCardExpiry());

        SimpleDateFormat orderDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDateRaw = orderDateFormatter.parse(this.orderDate);

        // Convert to java.time types for proper comparison
        LocalDate orderDate = orderDateRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        YearMonth expiryDate = YearMonth.from(expiryDateRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        YearMonth orderYearMonth = YearMonth.from(orderDate);
        return orderYearMonth.isBefore(expiryDate);
    }

    public Boolean validateCvvValid() {
        String cvv = this.creditCardInformation.getCvv();
        return cvv != null && cvv.matches("\\d{3}");
    }

    public Boolean validateOrderTotal() {
        int computedCost = 0;

        for (Pizza pizza : this.pizzasInOrder) {
            int pizzaCost = pizza.getPriceInPence();
            computedCost = computedCost + pizzaCost;
        }

        return computedCost == this.priceTotalInPence;
    }

    public Boolean validatePizzaDefined() throws JsonProcessingException, HttpClientErrorException {
        Set<String> availablePizzaNames = downloadRestaurants.getAllPizzaNames();

        for (Pizza pizza : this.pizzasInOrder) {
            String pizzaName = pizza.getName();
            if (!availablePizzaNames.contains(pizzaName)) {
                return false;
            }
        }
        return true;
    }

    public Boolean validateMaxPizzaCount() {
        return (this.pizzasInOrder.size() < 5) && (!this.pizzasInOrder.isEmpty());
    }

    public Boolean validateSingleRestaurantsOrder() throws JsonProcessingException, HttpClientErrorException {
        Map<String, Restaurant> pizzaMapRestaurant = downloadRestaurants.createPizzaRestaurantMap();
        Set<String> restaurantSet = new HashSet<>();

        for (Pizza pizza : this.pizzasInOrder) {
            String pizzaName = pizza.getName();
            String restaurant = pizzaMapRestaurant.get(pizzaName).getName();

            restaurantSet.add(restaurant);
        }
        return restaurantSet.size() < 2;
    }

    public Boolean validateRestaurantOpen() throws JsonProcessingException, HttpClientErrorException, ParseException {
        if (!this.validateSingleRestaurantsOrder()) {
            return false;
        }

        Map<String, Restaurant> pizzaMapRestaurant = downloadRestaurants.createPizzaRestaurantMap();

        String pizzaName = this.pizzasInOrder.getFirst().getName();
        Restaurant restaurant = pizzaMapRestaurant.get(pizzaName);

        SimpleDateFormat orderDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date orderDateRaw = orderDateFormatter.parse(this.orderDate);

        SimpleDateFormat dayOfWeekFormatter = new SimpleDateFormat("E"); // 'E' for day of the week
        String dayOfWeek = dayOfWeekFormatter.format(orderDateRaw);

        return !restaurant.getOpeningDays().contains(dayOfWeek);
    }

    public Boolean validatePricePerPizza() throws JsonProcessingException, HttpClientErrorException {
        Map<String, Integer> pizzaMapRestaurant = downloadRestaurants.createPizzaPriceMap();

        for (Pizza pizza : this.pizzasInOrder) {
            Integer pizzaPrice = pizza.getPriceInPence();
            String pizzaName = pizza.getName();
            Integer stablishedPrice = pizzaMapRestaurant.get(pizzaName);

            if (!Objects.equals(stablishedPrice, pizzaPrice)) {
                return false;
            }
        }
        return true;
    }

    public Boolean validateNotEmptyOrder() {
        return (this.pizzasInOrder != null) && (!pizzasInOrder.isEmpty());
    }

    public Restaurant getRestaurant() throws JsonProcessingException, HttpClientErrorException {

        if (this.checkOrderStatus().getOrderValidationCode() == "NO ERROR") {
            Map<String, Restaurant> pizzaMapRestaurant = downloadRestaurants.createPizzaRestaurantMap();

            String pizzaName = this.pizzasInOrder.getFirst().getName();

            return pizzaMapRestaurant.get(pizzaName);
        }
        else{
            return null;
        }
    }
}