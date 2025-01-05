import ilp_cw1.com.example.ilp_cw1.model;

public class ClosestPointRectangle {

    public Coordinates closestCoordinatesOnSegment(Coordinates p1, Coordinates p2, Coordinates p) {
        Coordinates segment = p2.subtract(p1); // Vector from p1 to p2
        Coordinates pointVector = p.subtract(p1); // Vector from p1 to p

        double t = pointVector.dot(segment) / segment.magnitudeSquared(); // Projection
        t = Math.max(0, Math.min(1, t)); // Clamp t to [0, 1]

        return p1.add(segment.scale(t)); // Closest point on the segment
    }

    // Method to find the nearest point on the rectangle
    public Coordinates nearestCoordinatesOnRectangle(Coordinates[] vertices, Coordinates point) {
        double minDistance = Double.MAX_VALUE;
        Coordinates nearestCoordinates = null;

        // Loop through each edge of the rectangle
        for (int i = 0; i < vertices.length; i++) {
            Coordinates p1 = vertices[i];
            Coordinates p2 = vertices[(i + 1) % vertices.length]; // Next vertex (wrap around for last edge)

            Coordinates closest = closestCoordinatesOnSegment(p1, p2, point);
            double distance = Coordinates.distance(closest, point);

            if (distance < minDistance) {
                minDistance = distance;
                nearestCoordinates = closest;
            }
        }

        return nearestCoordinates;
    }
}
