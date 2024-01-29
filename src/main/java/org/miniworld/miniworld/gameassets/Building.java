package org.miniworld.miniworld.gameassets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.view.Polygon;

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

    public void draw(GraphicsContext context) {
        this.base.draw(context, Color.DARKGRAY, 2);
    }
}
