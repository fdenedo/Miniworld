package org.miniworld.miniworld;

import java.util.ArrayList;
import java.util.List;

public class SpatialGraph {
    List<Point> points;
    List<Segment> segments;

    public SpatialGraph() {
        points = new ArrayList<>();
        segments = new ArrayList<>();
    }

    public void addPoint(Point p) {
        this.points.add(p);
    }

    public boolean tryAddPoint(Point p) {
        if (this.containsPoint(p)) return false;
        addPoint(p);
        return true;
    }

    public boolean containsPoint(Point point) {
        return this.points.contains(point);
    }

    public boolean containsSegment(Segment s) {
        return this.segments.contains(s);
    }

    public void addSegment(Point p1, Point p2) {
        Segment s = new Segment(p1, p2);
        this.segments.add(s);
    }

    public void addSegment(Segment s) {
        this.segments.add(s);
    }

    public boolean tryAddSegment(Segment s) {
        if (s.p1.equals(s.p2) || this.containsSegment(s)) return false;
        addSegment(s);
        return true;
    }

    public void removeSegment(Segment segment) {
        segments.remove(segment);
    }

    public void removePoint(int pointIndex) {
        Point removedPoint = points.remove(pointIndex);
        List<Segment> segmentsContainingPoint = segments.stream()
                .filter(s -> s.includesPoint(removedPoint))
                .toList();
        for (Segment segment : segmentsContainingPoint) {
            this.removeSegment(segment);
        }
    }

    public Point getNearestPointToCoordinates(double x, double y, double threshold) {
        double closestDistance = Integer.MAX_VALUE;
        Point nearest = null;

        for (Point p : this.points) {
            // Feels like an unnecessary optimisation
            if (Math.abs(x - p.x) > closestDistance || Math.abs(y - p.y) > closestDistance) continue;
            double newDistance = Math.hypot(x - p.x, y - p.y);
            if (newDistance < threshold && newDistance < closestDistance) {
                nearest = p;
                closestDistance = newDistance;
            }
        }

        return nearest;
    }

    public static SpatialGraph dummyGraph() {
        Point p1 = new Point(100, 100);
        Point p2 = new Point(300, 300);
        Point p3 = new Point(220, 100);
        Point p4 = new Point(400, 300);

        SpatialGraph g = new SpatialGraph();
        g.points.add(p1);
        g.points.add(p2);
        g.points.add(p3);
        g.points.add(p4);

        g.addSegment(p1, p2);
        g.addSegment(p1, p3);
        g.addSegment(p2, p3);
        g.addSegment(p2, p4);

        return g;
    }
}
