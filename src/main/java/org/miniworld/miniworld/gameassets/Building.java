package org.miniworld.miniworld.gameassets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.view.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.miniworld.miniworld.utils.MathUtils.*;

public class Building implements WorldObject {
    Polygon base;
    double height;

    public Building(Polygon base, double height) {
        this.base = base;
        this.height = height;
    }

    public Polygon getBase() {
        return base;
    }

    public void draw(GraphicsContext context, Point viewpoint) {
        List<Point> top = Arrays.stream(this.base.getPoints())
                .map(p -> {
                    double distance = distance(p, viewpoint);
                    double perspectiveScale = 1.0 / Math.max(1.0 + distance, 500); // 500 is a random value right now, I need to make this
                    return add(p, scale(subtract(p, viewpoint), height * perspectiveScale));
                })
                .toList();
        Polygon ceiling = new Polygon(top.toArray(new Point[0]));
        List<Polygon> sides = new ArrayList<>();
        for (int i = 0; i < this.base.getPoints().length; i++) {
            int nextI = (i + 1) % this.base.getPoints().length;
            Polygon poly = new Polygon(
                new Point[]{
                    this.base.getPoints()[i],
                    this.base.getPoints()[nextI],
                    top.get(nextI),
                    top.get(i)
                }
            );
            sides.add(poly);
        }
        sides.sort((a, b) -> Double.compare(b.distanceToPoint(viewpoint), a.distanceToPoint(viewpoint)));

        this.base.draw(context, Color.WHITE, Color.BLACK, 1);
        sides.forEach(side -> side.draw(context, Color.WHITE, Color.BLACK, 1));
        ceiling.draw(context, Color.WHITE, Color.BLACK, 1);
    }
}
