package org.miniworld.miniworld.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.view.Envelope;
import org.miniworld.miniworld.view.Polygon;

import java.util.ArrayList;
import java.util.List;

public class World {

    SpatialGraph graph;
    double roadWidth;
    int roundness;
    List<Envelope> envelopes;
    Canvas worldCanvas;

    public World(Canvas canvas, SpatialGraph graph, double roadWidth, int roundness) {
        this.worldCanvas = canvas;
        this.graph = graph;
        this.roadWidth = roadWidth;
        this.roundness = roundness;

        this.envelopes = new ArrayList<>();
    }

    public void generate() {
        this.envelopes = new ArrayList<>();

        for (Segment segment : this.graph.segments) {
            this.envelopes.add(new Envelope(segment, roadWidth, roundness));
        }

        Polygon.breakSegments(
                this.envelopes.get(0).getPolygon(),
                this.envelopes.get(1).getPolygon()
        );
    }

    private void drawBackground(GraphicsContext context) {
        context.setFill(Color.LIGHTBLUE);
        context.fillRect(0, 0, worldCanvas.getWidth(), worldCanvas.getHeight());
    }

    public void draw(GraphicsContext context) {
        drawBackground(context);
        for (Envelope envelope : this.envelopes) {
            envelope.draw(context);
//            envelope.getPolygon().drawAlternate(context); // ALTERNATE FOR DEBUGGING ONLY
        }
    }
}
