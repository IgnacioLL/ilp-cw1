package com.example.ilp_cw1.utils;

import static com.example.ilp_cw1.Constants.DISTANCE_MOVEMENT_DRONE;
import java.util.Random;

public class RandomPath {

    // Method to generate random samples within a rectangle
    public static double[][] samplePoints(double[][] rectangle, int numSamples) {
        double minLat = rectangle[0][0];
        double maxLat = rectangle[2][0];
        double minLong = rectangle[0][1];
        double maxLong = rectangle[1][1];

        Random random = new Random();
        double[][] samples = new double[numSamples][2];

        for (int i = 0; i < numSamples; i++) {
            // Generate random latitude and longitude within the rectangle
            double randomLat = minLat + (maxLat - minLat) * random.nextDouble();
            double randomLong = minLong + (maxLong - minLong) * random.nextDouble();

            // Project the values to be multiples of 0.00015
            randomLat = projectToModule(randomLat, DISTANCE_MOVEMENT_DRONE);
            randomLong = projectToModule(randomLong, DISTANCE_MOVEMENT_DRONE);


            samples[i][0] = randomLat;
            samples[i][1] = randomLong;
        }
        return samples;
    }

    // Method to project a value to the nearest multiple of module
    public static double projectToModule(double value, double module) {
        return Math.round(value / module) * module;
    }
}
