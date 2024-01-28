package org.miniworld.miniworld.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.model.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Envelope {
    Segment segment;
    double thickness;
    Polygon polygon;
    BoundingBox boundingBox;

    public Envelope(Segment segment, double thickness, int roundness) {
        this.segment = segment;
        this.thickness = thickness;

        int round = Math.max(1, roundness);

        this.polygon = createPolygon(round);

        this.boundingBox = calculateBoundingBox();
    }

    public Polygon getPolygon() {
        return this.polygon;
    }

    public Polygon createPolygon(int roundness) {
        Point p1 = segment.getP1();
        Point p2 = segment.getP2();

        double radius = thickness / 2;

        double alpha_cw = segment.angle() + Math.PI / 2;
        double alpha_acw = segment.angle() - Math.PI / 2;

        double step = Math.PI / roundness;
        double eps = step / 2; // ensures that the envelope always includes the last point

        List<Point> points = new ArrayList<>();
        for(double i = alpha_acw; i <= alpha_cw + eps; i += step) {
            points.add(p1.translate(Math.PI + i, radius));
        }
        for(double i = alpha_acw; i <= alpha_cw + eps; i += step) {
            points.add(p2.translate(i, radius));
        }

        return new Polygon(points.toArray(new Point[0]));
    }

    public static void breakSegments(Envelope envelope1, Envelope envelope2) {
        List<Segment> edges1 = envelope1.getPolygon().edges;
        List<Segment> edges2 = envelope2.getPolygon().edges;

        for (int i = 0; i < edges1.size(); i++) {
            for (int j = 0; j < edges2.size(); j++) {
                Segment e1 = edges1.get(i);
                Segment e2 = edges2.get(j);
                Optional<Point> intersection = Segment.getIntersection(e1, e2);

                if (intersection.isEmpty()) continue;
                Point p = intersection.get();
                if (e1.includesEndpoint(p) || e2.includesEndpoint(p)) continue;

                Point e1PrevP2 = e1.getP2();
                e1.setP2(p);
                edges1.add(i + 1, new Segment(p, e1PrevP2));

                Point e2PrevP2 = e2.getP2();
                e2.setP2(p);
                edges2.add(j + 1, new Segment(p, e2PrevP2));
            }
        }
    }

    public static void breakAll(List<Envelope> envelopes) {
        for (int i = 0; i < envelopes.size() - 1; i++) {
            for (int j = i + 1; j < envelopes.size(); j++) {
                Envelope e1 = envelopes.get(i);
                Envelope e2 = envelopes.get(j);
                // If the bounding boxes don't touch they can't possibly interact
                if (!e1.boundingBox.intersectsBoundingBox(e2.boundingBox)) continue;
                breakSegments(envelopes.get(i), envelopes.get(j));
            }
        }
    }

    public static List<Segment> union(List<Envelope> envelopes) {
        Envelope.breakAll(envelopes);
        List<Segment> segmentsToKeep = new ArrayList<>();

        for (int i = 0; i < envelopes.size(); i++) {
            for (Segment edge : envelopes.get(i).getPolygon().edges) {
                boolean keep = true;
                for (int j = 0; j < envelopes.size(); j++) {
                    if (i == j) continue;
                    if (envelopes.get(j).getPolygon().containsEdge(edge)) {
                        keep = false;
                        break;
                    }
                }
                if (keep) {
                    segmentsToKeep.add(edge);
                }
            }
        }
        return segmentsToKeep;
    }

    public void draw(GraphicsContext context) {
        this.polygon.draw(context, Color.color(0.7, 0.7, 0.7), 12);
    }

    public void drawGhost(GraphicsContext context) {
        this.polygon.draw(context, Color.color(0.1, 0.1, 0.8, 0.5), 1);
    }

    private BoundingBox calculateBoundingBox() {
        double minX = Math.min(segment.getP1().getX(), segment.getP2().getX()) - thickness;
        double minY = Math.min(segment.getP1().getY(), segment.getP2().getY()) - thickness;
        double maxX = Math.max(segment.getP1().getX(), segment.getP2().getX()) + thickness;
        double maxY = Math.max(segment.getP1().getY(), segment.getP2().getY()) + thickness;

        return new BoundingBox(new Point(minX, minY), new Point(maxX, maxY));
    }
}
