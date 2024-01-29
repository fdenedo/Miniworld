package org.miniworld.miniworld.gameassets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.view.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.miniworld.miniworld.utils.MathUtils.*;

public class Building {
    Polygon base;
    double heightCoef;

    public Building(Polygon base, double heightCoef) {
        this.base = base;
        this.heightCoef = heightCoef;
    }

    public Polygon getBase() {
        return base;
    }

    public void draw(GraphicsContext context, Point viewpoint) {
        List<Point> top = Arrays.stream(this.base.getPoints())
                .map(p -> add(p, scale(subtract(p, viewpoint), heightCoef)))
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
