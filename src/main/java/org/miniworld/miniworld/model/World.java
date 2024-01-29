package org.miniworld.miniworld.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.gameassets.Building;
import org.miniworld.miniworld.gameassets.Tree;
import org.miniworld.miniworld.gameassets.WorldObject;
import org.miniworld.miniworld.utils.MathUtils;
import org.miniworld.miniworld.view.BoundingBox;
import org.miniworld.miniworld.view.Envelope;
import org.miniworld.miniworld.view.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.miniworld.miniworld.utils.MathUtils.distance;
import static org.miniworld.miniworld.utils.MathUtils.lerp;

public class World {

    SpatialGraph previousGraph;
    SpatialGraph graph;
    double roadWidth;
    int roundness;
    List<Envelope> envelopes;
    Canvas worldCanvas;
    List<Segment> roadBorders;
    List<Building> buildings;
    List<Tree> trees;
    double buildingWidth;
    double buildingMinLength;
    double spacing;
    double treeSize;

    public World(
            Canvas canvas,
            SpatialGraph graph,
            double roadWidth,
            int roundness,
            double buildingWidth,
            double buildingMinLength,
            double spacing,
            double treeSize

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

        this.treeSize = treeSize;
    }

    public void generate() {
        if (graph.equals(previousGraph)) return;

        this.envelopes = new ArrayList<>();

        for (Segment segment : this.graph.segments) {
            this.envelopes.add(new Envelope(segment, roadWidth, roundness));
        }

        this.roadBorders = Envelope.union(this.envelopes.stream().toList());
        this.buildings = generateBuildings();
        this.trees = generateTrees();
        this.previousGraph = graph.deepCopy();
    }

    private List<Tree> generateTrees() {
        double left = Double.MAX_VALUE;
        double right = Double.MIN_VALUE;
        double top = Double.MAX_VALUE;
        double bottom = Double.MIN_VALUE;

        // update bounding box based on envelopes
        for (Envelope envelope : this.envelopes) {
            BoundingBox envelopeBoundingBox = envelope.getBoundingBox();

            left = Math.min(left, envelopeBoundingBox.getTopLeft().getX());
            right = Math.max(right, envelopeBoundingBox.getBottomRight().getX());
            top = Math.min(top, envelopeBoundingBox.getTopLeft().getY());
            bottom = Math.max(bottom, envelopeBoundingBox.getBottomRight().getY());
        }

        // update bounding box based on road borders
        for (Segment border : this.roadBorders) {
            left = Math.min(left, Math.min(border.getP1().getX(), border.getP2().getX()));
            right = Math.max(right, Math.max(border.getP1().getX(), border.getP2().getX()));
            top = Math.min(top, Math.min(border.getP1().getY(), border.getP2().getY()));
            bottom = Math.max(bottom, Math.max(border.getP1().getY(), border.getP2().getY()));
        }

        // update bounding box based on buildings
        for (Building building : this.buildings) {
            BoundingBox buildingBoundingBox = building.getBase().getBoundingBox();

            left = Math.min(left, buildingBoundingBox.getTopLeft().getX());
            right = Math.max(right, buildingBoundingBox.getBottomRight().getX());
            top = Math.min(top, buildingBoundingBox.getTopLeft().getY());
            bottom = Math.max(bottom, buildingBoundingBox.getBottomRight().getY());
        }

        List<Polygon> illegal = Stream.concat(
            this.buildings.stream().map(Building::getBase),
            this.envelopes.stream().map(Envelope::getPolygon)
        )
        .toList();


        List<Tree> trees = new ArrayList<>();
        int tryCount = 0;

        while (tryCount < 100) {
            Point p = new Point(
                    lerp(left, right, Math.random()),
                    lerp(top, bottom, Math.random())
            );

            // make sure trees aren't generated inside geometry
            boolean keep = true;
            for (Polygon illegalP : illegal) {
                if (illegalP.containsPoint(p) || illegalP.distanceToPoint(p) < this.treeSize / 2) {
                    keep = false;
                    break;
                }
            }

            // make sure trees don't overlap each other
            if (keep) {
                for (Tree tree : trees) {
                    if (distance(tree.getCentre(), p) < this.treeSize) {
                        keep = false;
                        break;
                    }
                }
            }

            // avoid trees being generated in the middle of nowhere
            if (keep) {
                boolean closeToSomething = false;
                for (Polygon polygon : illegal) {
                    if (polygon.distanceToPoint(p) < treeSize * 2) {
                        closeToSomething = true;
                        break;
                    }
                }
                keep = closeToSomething;
            }

            if (keep) {
                trees.add(new Tree(p, treeSize, 28));
                tryCount = 0;
            }

            tryCount++;
        }

        return trees;
    }


    private List<Building> generateBuildings() {
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

        double eps = 0.001;
        for (int i = 0; i < bases.size() - 1; i++) {
            for (int j = 1; j < bases.size(); j++) {
                if (i == j) continue;
                if (bases.get(i).intersectsPolygon(bases.get(j)) || bases.get(i).distanceToPolygon(bases.get(j)) < this.spacing - eps) {
                    bases.remove(j);
                    j--;
                }
            }
        }

        return bases.stream().map(b -> new Building(b, 35)).toList();
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

    public void draw(GraphicsContext context, Point viewpoint) {
        Random treeRNG = new Random(Tree.SEED);

        drawBackground(context);
        for (Envelope envelope : this.envelopes) {
            envelope.draw(context);
        }
        for (Segment border : this.roadBorders) {
            drawSegment(context, border);
        }

        List<WorldObject> worldObjects = new ArrayList<>(Stream
                .concat(
                        this.trees.stream(),
                        this.buildings.stream()
                )
                .toList());

        worldObjects.sort((a, b) -> Double.compare(b.getBase().distanceToPoint(viewpoint), a.getBase().distanceToPoint(viewpoint)));
        for (WorldObject obj : worldObjects) {
            if (obj instanceof Tree) {
                // TODO: Trees flicker now because they get drawn in a different order depending on how far they are from the current viewpoint
                ((Tree) obj).draw(context, viewpoint, treeRNG);
            } else {
                ((Building) obj).draw(context, viewpoint);
            }
        }
    }
}
