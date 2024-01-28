package org.miniworld.miniworld.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SpatialGraph {
    List<Point> points;
    List<Segment> segments;

    public SpatialGraph() {
        points = new ArrayList<>();
        segments = new ArrayList<>();
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
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
                .filter(s -> s.includesEndpoint(point))
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

    public SpatialGraph deepCopy() {
        SpatialGraph copy = new SpatialGraph();

        List<Point> copiedPoints = new ArrayList<>();
        for (Point point : this.points) {
            copiedPoints.add(new Point(point.x, point.y));
        }
        copy.points = copiedPoints;

        List<Segment> copiedSegments = new ArrayList<>();
        for (Segment segment : this.segments) {
            Point p1 = copiedPoints.get(this.points.indexOf(segment.p1));
            Point p2 = copiedPoints.get(this.points.indexOf(segment.p2));
            copiedSegments.add(new Segment(p1, p2));
        }
        copy.segments = copiedSegments;

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpatialGraph that = (SpatialGraph) o;

        if (new HashSet<>(points).equals(new HashSet<>(that.points))) {
            return new HashSet<>(segments).equals(new HashSet<>(that.segments));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(points, segments);
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
