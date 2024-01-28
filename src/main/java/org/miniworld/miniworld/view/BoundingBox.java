package org.miniworld.miniworld.view;

import org.miniworld.miniworld.model.Point;

public class BoundingBox {

    Point topLeft;
    Point bottomRight;
    double width;
    double height;

    public BoundingBox(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.width = bottomRight.getX() - topLeft.getX();
        this.height = bottomRight.getY() - topLeft.getY();
    }

    public boolean pointInBoundingBox(Point p) {
        return  p.getX() > topLeft.getX()
                && p.getX() < bottomRight.getX()
                && p.getY() > topLeft.getY()
                && p.getY() < bottomRight.getY();
    }

    public boolean intersectsBoundingBox(BoundingBox box) {
        return (Math.abs((this.topLeft.getX() + this.width/2) - (box.topLeft.getX() + box.width/2)) * 2 < (this.width + box.width)) &&
                (Math.abs((this.topLeft.getY() + this.height/2) - (box.topLeft.getY() + box.height/2)) * 2 < (this.height + box.height));
    }
}
