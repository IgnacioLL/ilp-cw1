package com.example.ilp_cw1.utils;

import java.util.ArrayList;
import java.util.List;
import com.example.ilp_cw1.model.Coordinates;
import static com.example.ilp_cw1.model.Coordinates.distance;

public class PathFinder {
    // Enum to represent cardinal directions with their vector components
    private enum Direction {
        N(0, 1),
        NNE(0.383, 0.924),
        NE(0.707, 0.707),
        ENE(0.924, 0.383),
        E(1, 0),
        ESE(0.924, -0.383),
        SE(0.707, -0.707),
        SSE(0.383, -0.924),
        S(0, -1),
        SSW(-0.383, -0.924),
        SW(-0.707, -0.707),
        WSW(-0.924, -0.383),
        W(-1, 0),
        WNW(-0.924, 0.383),
        NW(-0.707, 0.707),
        NNW(-0.383, 0.924);

        private final double dx;
        private final double dy;

        Direction(double dx, double dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    public List<Coordinates> findPath(Coordinates start, Coordinates end, double stepSize) {
        List<Coordinates> path = new ArrayList<>();
        path.add(start);  // Add starting point

        double currentLon = start.getLng();
        double currentLat = start.getLat();

        while (distance(start, end) > stepSize) {
            // Calculate direction vector to target
            double dx = end.getLng() - currentLon;
            double dy = end.getLat() - currentLat;

            // Normalize the direction vector
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;

            // Find the closest cardinal direction
            Direction bestDirection = null;
            double bestSimilarity = -1;

            for (Direction dir : Direction.values()) {
                // Use dot product to measure similarity
                double similarity = dx * dir.dx + dy * dir.dy;
                if (similarity > bestSimilarity) {
                    bestSimilarity = similarity;
                    bestDirection = dir;
                }
            }

            // Move one step in the chosen direction
            assert bestDirection != null;
            currentLon += bestDirection.dx * stepSize;
            currentLat += bestDirection.dy * stepSize;

            // Add new Coordinates to path
            path.add(new Coordinates(currentLon, currentLat));
        }

        return path;
    }

    public static void main(String[] args) {
        PathFinder pathFinder = new PathFinder();

        // Example: New York to Boston
        Coordinates startPoint = new Coordinates(-74.006, 40.7128);  // New York
        Coordinates endPoint = new Coordinates(-71.0589, 42.3601);   // Boston
        double stepSize = 0.00015;

        List<Coordinates> path = pathFinder.findPath(startPoint, endPoint, stepSize);

        System.out.println("Path from " + startPoint + " to " + endPoint);
        System.out.println("Number of steps: " + path.size());
        System.out.println("\nFirst 5 coordinates:");
        for (int i = 0; i < Math.min(5, path.size()); i++) {
            System.out.println(path.get(i));
        }

        // Calculate total distance covered
        double totalDistance = (path.size() - 1) * stepSize;
        System.out.printf("Total distance covered: %.4f degrees%n", totalDistance);
    }
}