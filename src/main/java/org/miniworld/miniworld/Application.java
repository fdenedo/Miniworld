package org.miniworld.miniworld;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        SpatialGraph graph = SpatialGraph.dummyGraph();

        Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGraph(graph, gc);

        root.getChildren().add(canvas);

        // BUTTONS (REMOVE LATER)
        HBox buttons = new HBox();

        Button addPointBtn = new Button("Add Point");
        addPointBtn.setOnAction(e -> {
            Point point = new Point((int) (Math.random() * canvas.getWidth()), (int) (Math.random() * canvas.getHeight()));
            if (!graph.tryAddPoint(point)) return;
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawGraph(graph, gc);
        });
        buttons.getChildren().add(addPointBtn);

        Button addSegmentBtn = new Button("Add Segment");
        addSegmentBtn.setOnAction(e -> {
            List<Point> points = graph.points;
            int index1 = (int) (Math.random() * points.size());
            int index2 = (int) (Math.random() * points.size());
            if (index1 == index2) return;
            Point p1 = points.get(index1);
            Point p2 = points.get(index2);

            if (!graph.tryAddSegment(new Segment (p1, p2))) return;

            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawGraph(graph, gc);
        });
        buttons.getChildren().add(addSegmentBtn);

        Button removePointBtn = new Button("Remove Point");
        removePointBtn.setOnAction(e -> {
            List<Point> points = graph.points;
            if (points.isEmpty()) return;

            int randomPointIndex = (int) (Math.random() * points.size());
            graph.removePoint(randomPointIndex);

            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawGraph(graph, gc);
        });
        buttons.getChildren().add(removePointBtn);

        Button removeSegmentBtn = new Button("Remove Segment");
        removeSegmentBtn.setOnAction(e -> {
            List<Segment> segments = graph.segments;
            if (segments.isEmpty()) return;

            int randomSegmentIndex = (int) (Math.random() * segments.size());
            graph.removeSegment(segments.get(randomSegmentIndex));

            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawGraph(graph, gc);
        });
        buttons.getChildren().add(removeSegmentBtn);

        // Add buttons to root
        root.getChildren().add(buttons);

        Scene scene = new Scene(root, 600, 600, Color.WHITESMOKE);

        stage.setTitle("Graph");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void drawGraph(SpatialGraph graph, GraphicsContext gc) {
        for (Segment segment : graph.segments) {
            gc.beginPath();
            gc.moveTo(segment.p1.x, segment.p1.y);
            gc.lineTo(segment.p2.x, segment.p2.y);
            gc.stroke();
        }

        for (Point point : graph.points) {
            gc.setFill(Color.BLACK);
            gc.fillOval(point.x - 6, point.y - 6, 12, 12);
        }
    }
}