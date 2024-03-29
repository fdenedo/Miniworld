package org.miniworld.miniworld.gameassets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.utils.MathUtils;
import org.miniworld.miniworld.view.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.miniworld.miniworld.utils.MathUtils.*;

public class Tree implements WorldObject {
    public static final long SEED = 123456789L;

    Point centre;
    double size;
    double height;
    Polygon base;

    public Tree(Point centre, double size, double height) {
        this.centre = centre;
        this.size = size;
        this.height = height;
        this.base = base(centre, size);
    }

    public Polygon getBase() {
        return this.base;
    }

    public Point getCentre() {
        return centre;
    }

    public void draw(GraphicsContext context, Point viewpoint, Random treeRNG) {
        Point difference = subtract(this.centre, viewpoint);
        double distance = distance(this.centre, viewpoint);
        double perspectiveScale = 1.0 / Math.max(1.0 + distance, 500);
        Point top = MathUtils.add(this.centre, scale(difference, height * perspectiveScale));

        double levelCount = 7;
        for (int level = 0; level < levelCount; level++) {
            double t = level / (levelCount - 1);
            Point point = lerp2D(this.centre, top, t);
            double levelSize = lerp(size, 8, t);
            Color color = Color.color(
                    0.2,
                    lerp(0.4, 1, t),
                    0.4
            );
            Polygon poly = generateTreeLevel(point, levelSize, treeRNG);
            if (level == 0) this.base = poly;
            poly.draw(context, color, 0);
        }
    }

    private Polygon generateTreeLevel(Point point, double size, Random treeRNG) {
        List<Point> points = new ArrayList<>();
        double radius = size / 2;
        for (double a = 0; a < 2 * Math.PI; a += Math.PI / 8) {
            double noisyRadius = radius * lerp(0.5, 1, treeRNG.nextDouble());
            points.add(point.translate(a, noisyRadius));
        }
        return new Polygon(points.toArray(new Point[0]));
    }

    private Polygon base(Point point, double size) {
        List<Point> points = new ArrayList<>();
        double radius = size / 2;
        double sizeCoef = 0.75;
        for (double a = 0; a < 2 * Math.PI; a += Math.PI / 16) {
            points.add(point.translate(a, radius * sizeCoef));
        }
        return new Polygon(points.toArray(new Point[0]));
    }

}
