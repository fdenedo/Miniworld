package org.miniworld.miniworld.gameassets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.utils.MathUtils;

import static org.miniworld.miniworld.utils.MathUtils.*;

public class Tree {
    Point centre;
    double size;
    double heightCoef;

    public Tree(Point centre, double size, double heightCoef) {
        this.centre = centre;
        this.size = size;
        this.heightCoef = heightCoef;
    }

    public Point getCentre() {
        return centre;
    }

    public void draw(GraphicsContext context, Point viewpoint) {
        Point difference = subtract(this.centre, viewpoint);

        Point top = MathUtils.add(this.centre, scale(difference, heightCoef));

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
            context.setFill(color);
            context.fillOval(point.getX() - levelSize / 2, point.getY() - levelSize / 2, levelSize, levelSize);
        }
    }
}
