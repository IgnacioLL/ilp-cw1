package com.example.ilp_cw1.utils;
import com.example.ilp_cw1.Constants;

import static com.example.ilp_cw1.Constants.MIN_RECTANGLE_WIDTH;
import static com.example.ilp_cw1.Constants.MIN_RECTANGLE_HEIGHT;


public class RectangleCreator {

    public static double[][] createRectangle(double lat1, double long1, double lat2, double long2) {
        // Determine min and max coordinates
        double minLat = Math.min(lat1, lat2);
        double maxLat = Math.max(lat1, lat2);
        double minLong = Math.min(long1, long2);
        double maxLong = Math.max(long1, long2);

        // Ensure minimum width and height
        if ((maxLat - minLat) < MIN_RECTANGLE_HEIGHT) {
            maxLat = minLat + MIN_RECTANGLE_HEIGHT;
        }
        if ((maxLong - minLong) < MIN_RECTANGLE_WIDTH) {
            maxLong = minLong + MIN_RECTANGLE_WIDTH;
        }

        // Define the rectangle corners
        return new double[][]{
                {minLat, minLong},  // Bottom-left
                {minLat, maxLong},  // Bottom-right
                {maxLat, minLong},  // Top-left
                {maxLat, maxLong}   // Top-right
        };
    }

}
