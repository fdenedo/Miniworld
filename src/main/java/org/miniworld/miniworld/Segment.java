package org.miniworld.miniworld;

import java.util.Objects;

public class Segment {
    Point p1, p2;

    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean includesPoint(Point p) {
        return this.p1.equals(p) || this.p2.equals(p);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment segment = (Segment) o;
        return this.includesPoint(segment.p1) && this.includesPoint(segment.p2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }
}
