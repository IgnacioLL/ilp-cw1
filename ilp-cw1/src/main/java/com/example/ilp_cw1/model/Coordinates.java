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

    // Subtract two points (as vectors)
    public Coordinates subtract(Coordinates p) {
        return new Coordinates(this.lng - p.lng, this.lat - p.lat);
    }

    // Add two points (as vectors)
    public Coordinates add(Coordinates p) {
        return new Coordinates(this.lng + p.lng, this.lat + p.lat);
    }

    // Scale a point/vector by a scalar
    public Coordinates scale(double scalar) {
        return new Coordinates(this.lng * scalar, this.lat * scalar);
    }

    // Dot product of two vectors
    public double dot(Coordinates p) {
        return this.lng * p.lng + this.lat * p.lat;
    }

    // Squared magnitude of a vector
    public double magnitudeSquared() {
        return this.lng * this.lng + this.lat * this.lat;
    }

    // Euclidean distance between two points
    public static double distance(Coordinates p1, Coordinates p2) {
        return Math.sqrt(Math.pow(p1.lng - p2.lng, 2) + Math.pow(p1.lat - p2.lat, 2));
    }
}