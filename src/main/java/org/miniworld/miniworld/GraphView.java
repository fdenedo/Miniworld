package org.miniworld.miniworld;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GraphView extends Canvas {
    private static final int POINT_DIAMETER = 12;
    private static final int POINT_RADIUS = POINT_DIAMETER / 2;
    private static final Color POINT_COLOUR = Color.BLACK;

    SpatialGraph graph;
    GraphicsContext context;

    public GraphView(double initialHeight, double initialWidth, SpatialGraph graph) {
        this.setHeight(initialHeight);
        this.setWidth(initialWidth);
        this.graph = graph;
        this.context = this.getGraphicsContext2D();
    }

    public GraphicsContext getContext() {
        return this.context;
    }

    public void drawGraph() {
        for (Segment segment : graph.segments) {
            context.beginPath();
            context.moveTo(segment.p1.x, segment.p1.y);
            context.lineTo(segment.p2.x, segment.p2.y);
            context.stroke();
        }

        context.setFill(POINT_COLOUR);
        for (Point point : graph.points) {
            context.fillOval(
                    point.x - POINT_RADIUS,
                    point.y - POINT_RADIUS,
                    POINT_DIAMETER,
                    POINT_DIAMETER
            );
        }
    }
}
