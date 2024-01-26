package org.miniworld.miniworld;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Polygon {
    Point[] points;

    public Polygon(Point[] points) {
        this.points = points;
    }

    public void draw(GraphicsContext context) {
        context.setStroke(Color.BLUE);
        context.setFill(Color.color(0.2, 0.2, 1.0, 0.5));
        context.setLineWidth(2);
        context.beginPath();
        context.moveTo(points[0].x, points[0].y);

        for (int i = 1; i <= points.length; i++) {
            int index = i % points.length;
            context.lineTo(points[index].x, points[index].y);
        }

        context.stroke();
        context.fill();
    }
}
