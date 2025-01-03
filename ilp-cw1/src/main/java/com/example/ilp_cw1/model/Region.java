package com.example.ilp_cw1.model;

import java.util.List;
public class Region {
    private String name;
    private List<Coordinates> vertices;


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Coordinates> getVertices() {
        return this.vertices;
    }

    public void setVertices(List<Coordinates> vertices){
        this.vertices = vertices;
    }

    public Boolean checkAllCoordinates() {
        int len = this.vertices.size();
        for (int i = 0; i < len; i++){
            if (this.vertices.get(i).checkCoordinates()==Boolean.FALSE) {
                return false;
            }
        }
        return true;
    }

    // Method to check if the points form a polygon
    public boolean checkPolygon() {
        // Step 1: Ensure there are at least 3 points
        if (this.vertices.size() < 3) {
            return false;
        }

        // Step 2: Check for non-collinearity by calculating the area of triangles
        for (int i = 1; i < this.vertices.size() - 1; i++) {
            if (triangleArea(this.vertices.get(i - 1), this.vertices.get(i), this.vertices.get(i + 1)) != 0) {
                return true; // The points are non-collinear, thus forming a polygon
            }
        }
        // If all points are collinear, they do not form a valid polygon
        return false;
    }

    // Method to calculate the area of a triangle formed by 3 points
    private double triangleArea(Coordinates p1, Coordinates p2, Coordinates p3) {
        return Math.abs(p1.lng * (p2.lat - p3.lat) + p2.lng * (p3.lat - p1.lat) + p3.lng * (p1.lat - p2.lat)) / 2.0;
    }
    public double[][] convertRegionToMatrix(){
        int len = this.vertices.size();
        double[][] region_matrix =  new double[len][2];
        for(int i = 0; i < len; i++) {
            // To avoid equalities will sum epsilon
            region_matrix[i][0] = this.vertices.get(i).lng;
            region_matrix[i][1] = this.vertices.get(i).lat;
        }

        return region_matrix;
    }


}
