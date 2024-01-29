package org.miniworld.miniworld.gameassets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.view.Polygon;

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
        this.base.draw(context, Color.WHITE, 2);
        ceiling.draw(context, Color.WHITE, 2);
    }
}
