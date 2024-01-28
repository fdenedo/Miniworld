package org.miniworld.miniworld.model;

import java.util.Objects;
import java.util.Optional;

import static org.miniworld.miniworld.utils.MathUtils.normalise;
import static org.miniworld.miniworld.utils.MathUtils.subtract;

public class Segment {
    Point p1, p2;

    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getP1() {
        return this.p1;
    }

    public Point getP2() {
        return this.p2;
    }

    public void setP1(Point p) {
        this.p1 = p;
    }

    public void setP2(Point p) {
        this.p2 = p;
    }

    public static Optional<Point> getIntersection(Segment segment1, Segment segment2) {
        double x1 = segment1.p1.x;
        double y1 = segment1.p1.y;
        double x2 = segment1.p2.x;
        double y2 = segment1.p2.y;

        double x3 = segment2.p1.x;
        double y3 = segment2.p1.y;
        double x4 = segment2.p2.x;
        double y4 = segment2.p2.y;

        // Check for parallel lines
        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        double eps = 0.001;

        if (Math.abs(denominator) < eps) {
            // Lines are parallel
            return Optional.empty();
        }

        // Calculate intersection point
        double intersectionX = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denominator;
        double intersectionY = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denominator;

        Point intersectionPoint = new Point(intersectionX, intersectionY);

        // Check if the intersection point is within the bounds of both line segments
        if (segment1.isPointWithinSegment(intersectionPoint) && segment2.isPointWithinSegment(intersectionPoint)) {
            return Optional.of(intersectionPoint);
        } else {
            return Optional.empty();
        }
    }

    public boolean includesEndpoint(Point p) {
        return this.p1.equals(p) || this.p2.equals(p);
    }

    public double angle() {
        return Math.atan2(p2.y - p1.y, p2.x - p1.x);
    }

    public Point directionVector() {
        return normalise(subtract(p2, p1));
    }

    public Point midpoint() {
        return new Point(
                (p2.x + p1.x) / 2,
                (p2.y + p1.y) / 2
        );
    }

    public double length() {
        return Math.hypot(Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return this.includesEndpoint(segment.p1) && this.includesEndpoint(segment.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }

    @Override
    public String toString() {
        return this.p1.toString() + " -> " + this.p2.toString();
    }

    private boolean isPointWithinSegment(Point point) {
        double minX = Math.min(p1.x, p2.x);
        double maxX = Math.max(p1.x, p2.x);
        double minY = Math.min(p1.y, p2.y);
        double maxY = Math.max(p1.y, p2.y);

        return (point.x >= minX && point.x <= maxX && point.y >= minY && point.y <= maxY);
    }
}
