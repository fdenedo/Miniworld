package org.miniworld.miniworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private void removeSegmentsConnectedToPoint(Point point) {
        List<Segment> segmentsContainingPoint = segments.stream()
                .filter(s -> s.includesPoint(point))
                .toList();
        for (Segment segment : segmentsContainingPoint) {
            this.removeSegment(segment);
        }
    }

    public void removePoint(Point point) {
        Optional<Point> optionalPoint = points.stream()
                .filter(p -> p.equals(point))
                .findFirst();

        if (optionalPoint.isPresent()) {
            Point removedPoint = optionalPoint.get();
            points.remove(removedPoint);
            removeSegmentsConnectedToPoint(removedPoint);
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

    public void clear() {
        points = new ArrayList<>();
        segments = new ArrayList<>();
    }

    public static SpatialGraph dummyGraph() {
        Point p1 = new Point(900, 900);
        Point p2 = new Point(1100, 1000);
        Point p3 = new Point(1200, 1000);
        Point p4 = new Point(1200, 920);


        SpatialGraph g = new SpatialGraph();
        g.points.add(p1);
        g.points.add(p2);
        g.points.add(p3);
        g.points.add(p4);

        g.addSegment(p1, p2);
        g.addSegment(p2, p3);
        g.addSegment(p3, p4);

        return g;
    }
}
