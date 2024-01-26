package org.miniworld.miniworld;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class GraphView extends Canvas {
    private static final int POINT_DIAMETER = 14;
    private static final int POINT_RADIUS = POINT_DIAMETER / 2;
    private static final Color POINT_COLOUR = Color.BLACK;
    private static final Color POINT_SELECTED_COLOUR = Color.LIGHTYELLOW;

    SpatialGraph graph;
    GraphicsContext context;

    Point mouse;

    Point selected;
    Point previousSelected;
    Point hovered;
    Point dragging;

    public GraphView(double initialWidth, double initialHeight, SpatialGraph graph) {
        super(initialWidth, initialHeight);

        this.graph = graph;
        this.context = this.getGraphicsContext2D();

        this.mouse = new Point(0, 0);
        this.selected = null;
        this.hovered = null;

        // Selecting and Removing Points
        this.setOnMouseClicked(this::handleMouseClicked);

        // Hovering Points
        this.setOnMouseMoved(this::handleMouseMoved);

        // Dragging Points
        this.setOnMousePressed(this::handleMousePressed);
        this.setOnMouseDragged(this::handleMouseDragging);
        this.setOnMouseReleased(this::handleMouseReleased);
    }

    public GraphicsContext getContext() {
        return this.context;
    }

    public void handleMouseClicked(MouseEvent event) {
        System.out.println("Mouse Clicked at: (" + this.mouse.x + ", " + this.mouse.y + ")");

        switch (event.getButton()) {
            case PRIMARY -> {
                this.previousSelected = selected;
                if (this.hovered != null) {
                    this.selected = hovered;
                } else {
                    Point newPoint = new Point(this.mouse.x, this.mouse.y);
                    graph.addPoint(newPoint);
                    this.selected = newPoint;
                    this.hovered = newPoint;
                }
                if (previousSelected != null) {
                    graph.tryAddSegment(new Segment(previousSelected, selected));
                }
            }
            case SECONDARY -> {
                if (this.selected != null) {
                    this.selected = null;
                }
                else if (this.hovered != null) {
                    this.graph.removePoint(this.hovered);
                    if (this.hovered.equals(selected)) this.selected = null;
                    this.hovered = null;
                }
            }
            case NONE, MIDDLE, BACK, FORWARD -> {}
        }
    }

    public void handleMouseMoved(MouseEvent event) {
        this.mouse.x = event.getX();
        this.mouse.y = event.getY();
        this.hovered = this.graph.getNearestPointToCoordinates(this.mouse.x, this.mouse.y, POINT_RADIUS * 1.2);
    }

    public void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            this.dragging = hovered;
        }
    }

    public void handleMouseDragging(MouseEvent event) {
        if (this.dragging != null) {
            this.dragging.x = event.getX();
            this.dragging.y = event.getY();
        }
    }

    public void handleMouseReleased(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            this.dragging = null;
        }
    }

    public void clear() {
        this.graph.clear();
        this.selected = null;
        this.hovered = null;
    }

    public void draw() {
        drawBackground();
        drawGraph();
        if (this.selected != null) {
            drawIntendedSegment();
        }
    }

    private void drawBackground() {
        context.setFill(Color.LIGHTBLUE);
        context.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawGraph() {
        if (!graph.points.isEmpty()) {
            Polygon poly = new Polygon(context, graph.points.toArray(new Point[0]));
            poly.draw();
        }
        for (Segment segment : graph.segments) {
            drawSegment(segment);
        }
        for (Point point : graph.points) {
            drawPoint(point);
        }
    }

    private void drawIntendedSegment() {
        context.setStroke(POINT_COLOUR.deriveColor(1.0, 1.0, 1.0, 0.2));
        context.setLineWidth(2);
        context.beginPath();
        context.moveTo(this.selected.x, this.selected.y);
        Point destination = this.hovered != null ? this.hovered : this.mouse;
        context.lineTo(destination.x, destination.y);
        context.stroke();
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
