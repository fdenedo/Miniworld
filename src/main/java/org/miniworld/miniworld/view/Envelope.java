package org.miniworld.miniworld.view;

import javafx.scene.canvas.GraphicsContext;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.model.Segment;
import org.miniworld.miniworld.view.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Envelope {
    Segment segment;
    double thickness;
    Polygon polygon;

    public Envelope(Segment segment, double thickness, int roundness) {
        this.segment = segment;
        this.thickness = thickness;

        int round = Math.max(1, roundness);

        this.polygon = createPolygon(round);
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

    public void draw(GraphicsContext context) {
        this.polygon.draw(context);
    }
}
