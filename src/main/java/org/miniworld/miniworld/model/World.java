package org.miniworld.miniworld.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.utils.MathUtils;
import org.miniworld.miniworld.view.Envelope;
import org.miniworld.miniworld.view.Polygon;

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
    List<Polygon> buildings;
    double buildingWidth;
    double buildingMinLength;
    double spacing;

    public World(
            Canvas canvas,
            SpatialGraph graph,
            double roadWidth,
            int roundness,
            double buildingWidth,
            double buildingMinLength,
            double spacing

    ) {
        this.worldCanvas = canvas;
        this.graph = graph;
        this.roadWidth = roadWidth;
        this.roundness = roundness;

        this.envelopes = new ArrayList<>();
        this.roadBorders = new ArrayList<>();

        this.buildingWidth = buildingWidth;
        this.buildingMinLength = buildingMinLength;
        this.spacing = spacing;
    }

    public void generate() {
        if (graph.equals(previousGraph)) return;

        this.envelopes = new ArrayList<>();

        for (Segment segment : this.graph.segments) {
            this.envelopes.add(new Envelope(segment, roadWidth, roundness));
        }

        this.roadBorders = Envelope.union(this.envelopes.stream().toList());
        this.buildings = generateBuildings();
        this.previousGraph = graph.deepCopy();
    }

    public List<Polygon> generateBuildings() {
        List<Envelope> tmp = new ArrayList<>();

        for (Segment segment : this.graph.segments) {
            tmp.add(new Envelope(segment, this.roadWidth + this.buildingWidth + this.spacing * 2, this.roundness));
        }

        List<Segment> guides = Envelope.union(tmp.stream().toList());

        for (int i = 0; i < guides.size(); i++) {
            Segment s = guides.get(i);
            if (s.length() < this.buildingMinLength) {
                guides.remove(i);
                i--;
            }
        }

        List<Segment> supports = new ArrayList<>();
        for (Segment s : guides) {
            double length = s.length() + this.spacing;
            int buildingCount = (int) (length / (this.buildingMinLength + this.spacing));
            double buildingLength = (length / buildingCount) - this.spacing;

            Point direction = s.directionVector();

            Point p1 = s.getP1();
            Point p2 = MathUtils.add(p1, MathUtils.scale(direction, buildingLength));
            supports.add(new Segment(p1, p2));

            for (int i = 2; i <= buildingCount; i++) {
                p1 = MathUtils.add(p2, MathUtils.scale(direction, this.spacing));
                p2 = MathUtils.add(p1, MathUtils.scale(direction, buildingLength));
                supports.add(new Segment(p1, p2));
            }
        }

        List<Polygon> bases = new ArrayList<>();
        for (Segment s : supports) {
            bases.add(new Envelope(s, this.buildingWidth, 0).getPolygon());
        }

        for (int i = 0; i < bases.size() - 1; i++) {
            for (int j = 1; j < bases.size(); j++) {
                if (i == j) continue;
                if (bases.get(i).intersectsPolygon(bases.get(j))) {
                    bases.remove(j);
                    j--;
                }
            }
        }

        return bases;
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
        for (Polygon poly : this.buildings) {
            poly.draw(context, Color.DARKGRAY, 2);
        }
    }
}
