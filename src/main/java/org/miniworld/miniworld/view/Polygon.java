package org.miniworld.miniworld.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.model.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Polygon {
    Point[] points;
    List<Segment> edges;

    public Polygon(Point[] points) {
        this.points = points;
        this.edges = new ArrayList<>();
        for (int i = 1; i <= points.length; i++) {
            this.edges.add(new Segment(points[i - 1], points[i % points.length]));
        }
    }

    public static void breakSegments(Polygon polygon1, Polygon polygon2) {
        List<Segment> edges1 = polygon1.edges;
        List<Segment> edges2 = polygon2.edges;

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
                Point e2PrevP2 = e1.getP2();
                e2.setP2(p);
                edges2.add(j + 1, new Segment(p, e2PrevP2));
            }
        }
    }

    public void draw(GraphicsContext context) {
        context.setStroke(Color.BLUE);
        context.setFill(Color.color(0.2, 0.2, 1.0, 0.5));
        context.setLineWidth(2);
        context.beginPath();
        context.moveTo(points[0].getX(), points[0].getY());

        for (int i = 1; i <= points.length; i++) {
            int index = i % points.length;
            context.lineTo(points[index].getX(), points[index].getY());
        }

        context.stroke();
        context.fill();
    }
}
