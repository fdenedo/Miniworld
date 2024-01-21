package org.miniworld.miniworld;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GraphView extends Canvas {
    private static final int POINT_DIAMETER = 14;
    private static final int POINT_RADIUS = POINT_DIAMETER / 2;
    private static final Color POINT_COLOUR = Color.BLACK;
    private static final Color POINT_SELECTED_COLOUR = Color.LIGHTYELLOW;

    SpatialGraph graph;
    GraphicsContext context;

    Point selected;
    Point hovered;

    public GraphView(double initialHeight, double initialWidth, SpatialGraph graph) {
        this.setHeight(initialHeight);
        this.setWidth(initialWidth);
        this.graph = graph;
        this.context = this.getGraphicsContext2D();

        this.selected = null;
        this.hovered = null;

        this.setOnMouseClicked(this::handleMouseClicked);
        this.setOnMouseMoved(this::handleMouseMoved);
    }

    public GraphicsContext getContext() {
        return this.context;
    }

    public void handleMouseClicked(MouseEvent event) {
        Point p = new Point(event.getX(), event.getY());
        System.out.println("Mouse Clicked at: (" + p.x + ", " + p.y + ")");

        if (event.getButton() == MouseButton.SECONDARY) {
            if (this.hovered != null) {
                this.graph.removePoint(this.hovered);
                this.hovered = null;
            }
        } else {
            if (this.hovered != null) {
                this.selected = hovered;
            } else {
                this.selected = p;
                this.hovered = p;
                graph.addPoint(p);
            }
        }
    }

    public void handleMouseMoved(MouseEvent event) {
        this.hovered = this.graph.getNearestPointToCoordinates(event.getX(), event.getY(), POINT_RADIUS * 1.2);
    }

    public void drawGraph() {
        for (Segment segment : graph.segments) {
            drawSegment(segment);
        }

        for (Point point : graph.points) {
            drawPoint(point);
        }
    }

    private void drawSegment(Segment segment) {
        context.setStroke(POINT_COLOUR);
        context.setLineWidth(2);
        context.beginPath();
        context.moveTo(segment.p1.x, segment.p1.y);
        context.lineTo(segment.p2.x, segment.p2.y);
        context.stroke();
    }

    private void drawPoint(Point point) {
        context.setFill(POINT_COLOUR);
        context.fillOval(point.x - POINT_RADIUS, point.y - POINT_RADIUS, POINT_DIAMETER, POINT_DIAMETER);
        if (this.selected == point) {
            double selectedLineRatio = 0.6;
            context.setStroke(POINT_SELECTED_COLOUR);
            context.setLineWidth(2);
            context.strokeOval(
                    point.x - (POINT_RADIUS * selectedLineRatio),
                    point.y - (POINT_RADIUS * selectedLineRatio),
                    POINT_DIAMETER * selectedLineRatio,
                    POINT_DIAMETER * selectedLineRatio
            );
        } else if (this.hovered == point) {
            double hoveredRatio = 0.2;
            context.setStroke(POINT_SELECTED_COLOUR);
            context.setLineWidth(3);
            context.strokeOval(
                    point.x - (POINT_RADIUS * hoveredRatio),
                    point.y - (POINT_RADIUS * hoveredRatio),
                    POINT_DIAMETER * hoveredRatio,
                    POINT_DIAMETER * hoveredRatio
            );
        }
    }
}
