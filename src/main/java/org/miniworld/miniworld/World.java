package org.miniworld.miniworld;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
    }

    private void drawBackground(GraphicsContext context) {
        context.setFill(Color.LIGHTBLUE);
        context.fillRect(0, 0, worldCanvas.getWidth(), worldCanvas.getHeight());
    }

    public void draw(GraphicsContext context) {
        drawBackground(context);
        for (Envelope envelope : this.envelopes) {
            envelope.draw(context);
        }
    }
}