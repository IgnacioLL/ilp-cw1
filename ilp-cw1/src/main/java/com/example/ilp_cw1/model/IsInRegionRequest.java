package com.example.ilp_cw1.model;

public class IsInRegionRequest {
    public Coordinates position;
    public Region region;

    public Coordinates getPosition() {
        return this.position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Region getRegion() {
        return this.region;
    }
}
