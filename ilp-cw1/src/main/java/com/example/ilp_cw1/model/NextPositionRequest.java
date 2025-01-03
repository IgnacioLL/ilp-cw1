package com.example.ilp_cw1.model;

public class NextPositionRequest {
    private Coordinates start;
    private double angle;

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public double getAngle(){
        return this.angle;
    }

    public void setStart(Coordinates start){
        this.start = start;
    }

    public Coordinates getStart(){
        return this.start;
    }

}
