package org.miniworld.miniworld;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.model.SpatialGraph;
import org.miniworld.miniworld.model.World;
import org.miniworld.miniworld.utils.MathUtils;
import org.miniworld.miniworld.view.GraphView;
import org.miniworld.miniworld.view.Viewport;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        SpatialGraph graph = SpatialGraph.dummyGraph();

        GraphView graphView = new GraphView(2000, 2000, graph);
        World world = new World(graphView, graph, 40, 20, 50, 60, 18, 40);
        GraphicsContext gc = graphView.getContext();

        Viewport viewport = new Viewport(graphView);

        // FOR DEBUGGING
        viewport.heightProperty().addListener((observable, oldValue, newValue)
                -> System.out.println("Viewport Height Changed: " + newValue));
        viewport.widthProperty().addListener((observable, oldValue, newValue)
                -> System.out.println("Viewport Width Changed: " + newValue));
        // END DEBUGGING

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.clearRect(0, 0, graphView.getWidth(), graphView.getHeight());
                world.generate();
                Point viewpoint = viewport.getCanvasPointForViewportPoint(viewport.center());
                world.draw(gc, viewpoint);
                graphView.draw();
            }
        };
        animationTimer.start();

        root.getChildren().add(viewport);

        // BUTTONS
        HBox buttons = new HBox();

        Button clearAllBtn = new Button("Clear All");
        clearAllBtn.setOnAction(e -> graphView.clear());
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