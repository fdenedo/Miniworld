package org.miniworld.miniworld.gameassets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;
import org.miniworld.miniworld.model.Segment;
import org.miniworld.miniworld.utils.MathUtils;

import static org.miniworld.miniworld.utils.MathUtils.scale;
import static org.miniworld.miniworld.utils.MathUtils.subtract;

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

        context.setFill(Color.GREEN);
        context.setFill(Color.color(0.2, 0.9, 0.2, 0.6));
        context.fillOval(centre.getX() - size / 2, centre.getY() - size / 2, size, size);

        Point top = MathUtils.add(this.centre, scale(difference, heightCoef));
        Segment s = new Segment(this.centre, top);



        context.setStroke(Color.BLACK);
        context.setLineWidth(1);
        context.beginPath();
        context.moveTo(s.getP1().getX(), s.getP1().getY());
        context.lineTo(s.getP2().getX(), s.getP2().getY());
        context.stroke();
    }
}
