package org.miniworld.miniworld.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.view.Envelope;

import java.util.ArrayList;
import java.util.List;

public class World {

    SpatialGraph previousGraph;
    SpatialGraph graph;
    double roadWidth;
    int roundness;
    List<Envelope> envelopes;
    Canvas worldCanvas;
    List<Segment> roadBorders;

    public World(Canvas canvas, SpatialGraph graph, double roadWidth, int roundness) {
        this.worldCanvas = canvas;
        this.graph = graph;
        this.roadWidth = roadWidth;
        this.roundness = roundness;

        this.envelopes = new ArrayList<>();
        this.roadBorders = new ArrayList<>();
    }

    public void generate() {
        if (graph.equals(previousGraph)) return;

        this.envelopes = new ArrayList<>();

        for (Segment segment : this.graph.segments) {
            this.envelopes.add(new Envelope(segment, roadWidth, roundness));
        }

        this.roadBorders = Envelope.union(this.envelopes.stream().toList());
        this.previousGraph = graph.deepCopy();
    }

    private void drawBackground(GraphicsContext context) {
        context.setFill(Color.LIGHTBLUE);
        context.fillRect(0, 0, worldCanvas.getWidth(), worldCanvas.getHeight());
    }

    private void drawSegment(GraphicsContext context, Segment segment) {
        context.setStroke(Color.WHITE);
        context.setLineWidth(2);
        context.beginPath();
        context.moveTo(segment.getP1().getX(), segment.getP1().getY());
        context.lineTo(segment.getP2().getX(), segment.getP2().getY());
        context.stroke();
    }

    public void draw(GraphicsContext context) {
        drawBackground(context);
        for (Envelope envelope : this.envelopes) {
            envelope.draw(context);
        }
        for (Segment border : this.roadBorders) {
            drawSegment(context, border);
        }
    }
}
