package com.example.ilp_cw1.utils;
import static java.lang.Math.*;

// Extracted from: cdhttps://rosettacode.org/wiki/Ray-casting_algorithm#
public class RayCasting {

    static boolean intersects(double[] A, double[] B, double[] P) {
        if (A[1] > B[1])
            return intersects(B, A, P);

        if (P[1] == A[1] || P[1] == B[1])
            P[1] += 0.0001;

        if (P[1] > B[1] || P[1] < A[1] || P[0] >= max(A[0], B[0]))
            return false;

        if (P[0] < min(A[0], B[0]))
            return true;

        double red = (P[1] - A[1]) / (double) (P[0] - A[0]);
        double blue = (B[1] - A[1]) / (double) (B[0] - A[0]);
        return red >= blue;
    }

    public static boolean contains(double[][] shape, double[] pnt) {
        boolean inside = false;
        int len = shape.length;
        for (int i = 0; i < len; i++) {
            if (intersects(shape[i], shape[(i + 1) % len], pnt))
                inside = !inside;
        }
        return inside;
    }

    public static void main(String[] a) {
        double[][] testPoints = {{9.5784, 10}, {10, 16}, {-20, 10}, {0, 10},
                {20, 10}, {16, 10}, {20, 20}};

        for (double[][] shape : shapes) {
            for (double[] pnt : testPoints)
                System.out.printf("%7s ", contains(shape, pnt));
            System.out.println();
        }
    }

    final static double[][] square = {{0, 0.332}, {20, 0}, {20, 20}, {0, 20}};

    final static double[][] squareHole = {{0, 0}, {20, 0}, {20, 20}, {0, 20},
            {5, 5}, {15, 5}, {15, 15}, {5, 15}};

    final static double[][] strange = {{0, 0}, {5, 5}, {0, 20}, {5, 15}, {15, 15},
            {20, 20}, {20, 0}};

    final static double[][] hexagon = {{6, 0}, {14, 0}, {20, 10}, {14, 20},
            {6, 20}, {0, 10}};

    final static double[][][] shapes = {square, squareHole, strange, hexagon};
}