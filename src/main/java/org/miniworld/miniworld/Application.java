package org.miniworld.miniworld;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        SpatialGraph graph = SpatialGraph.dummyGraph();

        GraphView graphView = new GraphView(600, 600, graph);
        GraphicsContext gc = graphView.getContext();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, graphView.getWidth(), graphView.getHeight());
                graphView.drawGraph();
            }
        };
        animationTimer.start();

        root.getChildren().add(graphView);

        // BUTTONS (REMOVE LATER)
        HBox buttons = new HBox();

        Button addPointBtn = new Button("Add Point");
        addPointBtn.setOnAction(e -> {
            Point point = new Point((int) (Math.random() * graphView.getWidth()), (int) (Math.random() * graphView.getHeight()));
            if (!graph.tryAddPoint(point)) System.out.printf("Failed to add point (%s, %s)", point.x, point.y);
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

            if (!graph.tryAddSegment(new Segment (p1, p2))) System.out.println("Failed to add segment");
        });
        buttons.getChildren().add(addSegmentBtn);

        Button removePointBtn = new Button("Remove Point");
        removePointBtn.setOnAction(e -> {
            List<Point> points = graph.points;
            if (points.isEmpty()) return;

            int randomPointIndex = (int) (Math.random() * points.size());
            graph.removePoint(randomPointIndex);
        });
        buttons.getChildren().add(removePointBtn);

        Button removeSegmentBtn = new Button("Remove Segment");
        removeSegmentBtn.setOnAction(e -> {
            List<Segment> segments = graph.segments;
            if (segments.isEmpty()) return;

            int randomSegmentIndex = (int) (Math.random() * segments.size());
            graph.removeSegment(segments.get(randomSegmentIndex));
        });
        buttons.getChildren().add(removeSegmentBtn);

        // Add buttons to root
        root.getChildren().addAll(buttons);

        Scene scene = new Scene(root, 600, 630, Color.WHITESMOKE);

        stage.setTitle("Graph");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}