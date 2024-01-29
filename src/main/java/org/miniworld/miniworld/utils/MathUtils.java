package org.miniworld.miniworld.utils;

import org.miniworld.miniworld.model.Point;

public class MathUtils {
    public static Point add(Point p1, Point p2) {
        return new Point(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }

    public static Point subtract(Point p1, Point p2) {
        return new Point(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public static Point scale(Point p, double scalar) {
        return new Point(p.getX() * scalar, p.getY() * scalar);
    }

    public static Point normalise(Point p) {
        return scale(p, 1 / magnitude(p));
    }

    public static double dot(Point p1, Point p2) {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }

    public static double distance(Point p1, Point p2) {
        return Math.abs(Math.hypot(p1.getX() - p2.getX(), p1.getY() - p2.getY()));
    }

    public static double magnitude(Point p) {
        return Math.hypot(p.getX(), p.getY());
    }

    public static double lerp(double A, double B, double t) {
        return A + (B - A) * t;
    }
}
