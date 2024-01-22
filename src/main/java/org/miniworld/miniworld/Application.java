package org.miniworld.miniworld;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
                graphView.draw();
            }
        };
        animationTimer.start();

        root.getChildren().add(graphView);

        // BUTTONS
        HBox buttons = new HBox();

        Button clearAllBtn = new Button("Clear All");
        clearAllBtn.setOnAction(e -> graph.clear());
        buttons.getChildren().add(clearAllBtn);

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