package com.example.ilp_cw1.model;

import java.util.List;

public class Restaurant {
    private String name;
    private Coordinates coordinates;
    private List<String> openingDays;
    private List<Pizza> menu;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setLocation(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<String> getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(List<String> openingDays) {
        this.openingDays = openingDays;
    }

    public List<Pizza> getMenu() {
        return menu;
    }

    public void setMenu(List<Pizza> menu) {
        this.menu = menu;
    }

}
