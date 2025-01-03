package com.example.ilp_cw1.model;

public class OrderStatus {
    private String orderValidationCode;
    private String orderValidationResult;

    // Default constructor needed for JSON serialization
    public OrderStatus() {
    }

    public OrderStatus(String orderValidationCode, String orderValidationResult) {
        this.orderValidationCode = orderValidationCode;
        this.orderValidationResult = orderValidationResult;
    }

    // Getters and setters needed for JSON serialization
    public String getOrderValidationCode() {
        return orderValidationCode;
    }

    public void setOrderValidationCode(String orderValidationCode) {
        this.orderValidationCode = orderValidationCode;
    }

    public String getOrderValidationResult() {
        return orderValidationResult;
    }

    public void setOrderValidationResult(String orderValidationResult) {
        this.orderValidationResult = orderValidationResult;
    }
}