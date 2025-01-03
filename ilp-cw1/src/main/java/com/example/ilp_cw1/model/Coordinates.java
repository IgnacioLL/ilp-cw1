package com.example.ilp_cw1.model;

public class Coordinates {


    public double lng;
    public double lat;

    // Default constructor
    public Coordinates() {
        this.lng = 0.0;
        this.lat = 0.0;
    }

    // Parameterized constructor
    public Coordinates(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public Double getLng(){
        return lng;
    }
    public void setLng(Double lng){ this.lng = lng; }
    public Double getLat(){
        return lat;
    }

    public void setLat(Double lat){
        this.lat = lat;
    }

    public double[] convertCoordinatesToVector() {
        double[] position_vector = new double[2];
        position_vector[0] = this.lng;
        position_vector[1] = this.lat;

        return position_vector;
    }

    public Boolean checkCoordinates() {
        if (this.lng < -180 | this.lng > 180) {
            return false;
        } else if (this.lat < -90 | this.lat > 90) {
            return false;
        } else {
            return true;
        }
    }
}