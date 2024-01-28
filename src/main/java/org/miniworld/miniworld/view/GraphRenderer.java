package org.miniworld.miniworld.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.miniworld.miniworld.model.Point;

public class GraphRenderer {
    double pointDiameter;
    Color pointColor;
    Color selectedPointColor;

    public GraphicsContext context;

    public GraphRenderer(
            double pointDiameter,
            Color pointColor,
            Color selectedPointColor
    ) {
        this.pointDiameter = pointDiameter;
        this.pointColor = pointColor;
        this.selectedPointColor = selectedPointColor;
    }

    public double pointRadius() {
        return pointDiameter / 2;
    }

    private void drawPoint(Point point) {
        context.setFill(pointColor);
        context.fillOval(point.getX() - pointRadius(), point.getY() - pointRadius(), pointDiameter, pointDiameter);
    }

    private void drawSelectedPoint(Point point) {
        drawPoint(point);

        double selectedLineRatio = 0.6;
        context.setStroke(selectedPointColor);
        context.setLineWidth(2);
        context.strokeOval(
                point.getX() - (pointRadius() * selectedLineRatio),
                point.getY() - (pointRadius() * selectedLineRatio),
                pointDiameter * selectedLineRatio,
                pointDiameter * selectedLineRatio
        );
    }

    private void drawHoveredPoint(Point point) {
        drawPoint(point);

        double hoveredLineRatio = 0.2;
        context.setFill(selectedPointColor);
        context.setLineWidth(2);
        context.fillOval(
                point.getX() - (pointRadius() * hoveredLineRatio),
                point.getY() - (pointRadius() * hoveredLineRatio),
                pointDiameter * hoveredLineRatio,
                pointDiameter * hoveredLineRatio
        );
    }
}
