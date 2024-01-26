package org.miniworld.miniworld.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.model.Segment;
import org.miniworld.miniworld.model.SpatialGraph;

public class GraphView extends Canvas {
    private static final int POINT_DIAMETER = 14;
    private static final int POINT_RADIUS = POINT_DIAMETER / 2;
    private static final Color POINT_COLOUR = Color.BLACK;
    private static final Color POINT_SELECTED_COLOUR = Color.LIGHTYELLOW;

    SpatialGraph graph;
    GraphicsContext context;

    double mouseX, mouseY;

    Point selected;
    Point previousSelected;
    Point hovered;
    Point dragging;

    public GraphView(double initialWidth, double initialHeight, SpatialGraph graph) {
        super(initialWidth, initialHeight);

        this.graph = graph;
        this.context = this.getGraphicsContext2D();

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
        System.out.println("Mouse Clicked at: (" + this.mouseX + ", " + this.mouseY + ")");

        switch (event.getButton()) {
            case PRIMARY -> {
                this.previousSelected = selected;
                if (this.hovered != null) {
                    this.selected = hovered;
                } else {
                    Point newPoint = new Point(this.mouseX, this.mouseY);
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
        this.mouseX = event.getX();
        this.mouseY = event.getY();
        this.hovered = this.graph.getNearestPointToCoordinates(this.mouseX, this.mouseY, POINT_RADIUS * 1.2);
    }

    public void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            this.dragging = hovered;
        }
    }

    public void handleMouseDragging(MouseEvent event) {
        if (this.dragging != null) {
            this.dragging.setX(event.getX());
            this.dragging.setY(event.getY());
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
//        drawBackground();
        drawGraph();
        if (this.selected != null) {
            drawIntendedSegment();
        }
    }

    private void drawGraph() {
        for (Segment segment : graph.getSegments()) {
            drawSegment(segment);
        }
        for (Point point : graph.getPoints()) {
            drawPoint(point);
        }
    }

    private void drawIntendedSegment() {
        context.setStroke(POINT_COLOUR.deriveColor(1.0, 1.0, 1.0, 0.2));
        context.setLineWidth(2);
        context.beginPath();
        context.moveTo(this.selected.getX(), this.selected.getY());
        Point destination = this.hovered != null ? this.hovered : new Point(mouseX, mouseY);
        context.lineTo(destination.getX(), destination.getY());
        context.stroke();
    }

    private void drawSegment(Segment segment) {
        context.setStroke(POINT_COLOUR);
        context.setLineWidth(2);
        context.beginPath();
        context.moveTo(segment.getP1().getX(), segment.getP1().getY());
        context.lineTo(segment.getP2().getX(), segment.getP2().getY());
        context.stroke();
    }

    private void drawPoint(Point point) {
        context.setFill(POINT_COLOUR);
        context.fillOval(point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS, POINT_DIAMETER, POINT_DIAMETER);
        if (this.selected == point) {
            double selectedLineRatio = 0.6;
            context.setStroke(POINT_SELECTED_COLOUR);
            context.setLineWidth(2);
            context.strokeOval(
                    point.getX() - (POINT_RADIUS * selectedLineRatio),
                    point.getY() - (POINT_RADIUS * selectedLineRatio),
                    POINT_DIAMETER * selectedLineRatio,
                    POINT_DIAMETER * selectedLineRatio
            );
        } else if (this.hovered == point) {
            double hoveredRatio = 0.2;
            context.setStroke(POINT_SELECTED_COLOUR);
            context.setLineWidth(3);
            context.strokeOval(
                    point.getX() - (POINT_RADIUS * hoveredRatio),
                    point.getY() - (POINT_RADIUS * hoveredRatio),
                    POINT_DIAMETER * hoveredRatio,
                    POINT_DIAMETER * hoveredRatio
            );
        }
    }
}
