package org.miniworld.miniworld.view;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Pane;
import org.miniworld.miniworld.model.Point;

public class Viewport extends Pane {
    private static final double MAX_ZOOM_IN = 1;
    private static final double MAX_ZOOM_OUT = 5;
    private static final double ZOOM_STEP_AMOUNT = 0.1;

    Canvas canvas;
    double zoom;
    boolean panning;
    double mouseX, mouseY;

    public Viewport(Canvas canvas) {
        this.canvas = canvas;
        this.zoom = 1;
        this.panning = false;

        this.getChildren().add(canvas);

        this.setOnScroll(this::handleScroll);
        this.setOnZoom(this::handleZoom);

        this.setOnMousePressed(this::handleMousePressed);
        this.setOnMouseDragged(this::handleMouseDragging);
        this.setOnMouseReleased(this::handleMouseReleased);

        centreCanvas();
    }

    public Point getCanvasTopLeftPosition() {
        Point2D canvasLocal = canvas.localToParent(0, 0);

        return new Point(canvasLocal.getX(), canvasLocal.getY());
    }

    public Point getCanvasCenterPosition() {
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        Point2D canvasCenterLocal = canvas.localToParent(centerX, centerY);

        return new Point(canvasCenterLocal.getX(), canvasCenterLocal.getY());
    }

    public double getScaledWidthOfCanvas() {
        return canvas.getWidth() * canvas.getScaleX();
    }

    public double getScaledHeightOfCanvas() {
        return canvas.getHeight() * canvas.getScaleY();
    }

    public void handleScroll(ScrollEvent event) {
        double scaleDelta = (event.getDeltaY() < 0 ? -1.0 : 1.0) * ZOOM_STEP_AMOUNT;

        this.zoom -= scaleDelta;
        this.zoom = Math.max(MAX_ZOOM_IN, Math.min(this.zoom, MAX_ZOOM_OUT));

        applyZoom();
        event.consume();
        System.out.println("Canvas Position: " + getCanvasTopLeftPosition());
        System.out.println("Position of Centre of Canvas: " + getCanvasCenterPosition());
    }

    public void handleZoom(ZoomEvent event) {
        this.zoom *= event.getZoomFactor();
        this.zoom = Math.max(MAX_ZOOM_IN, Math.min(this.zoom, MAX_ZOOM_OUT));

        applyZoom();
        event.consume();
        System.out.println(this.zoom);
    }

    public void handleMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.MIDDLE) {
            this.panning = true;
            Point2D localCoords = sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
            this.mouseX = localCoords.getX();
            this.mouseY = localCoords.getY();
        }
    }

    public void handleMouseDragging(MouseEvent event) {
        if (this.panning) {
            Point2D localCoords = sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
            double deltaX = localCoords.getX() - this.mouseX;
            double deltaY = localCoords.getY() - this.mouseY;

            applyPanning(deltaX, deltaY);

            this.mouseX = localCoords.getX();
            this.mouseY = localCoords.getY();
        }
    }

    public void handleMouseReleased(MouseEvent event) {
        if (event.getButton() == MouseButton.MIDDLE) {
            this.panning = false;
        }
    }

    private void centreCanvas() {
        double viewportCentreX = getWidth() / 2;
        double viewportCentreY = getHeight() / 2;
        Point canvasCentre = getCanvasCenterPosition();
        double canvasX = canvasCentre.getX();
        double canvasY = canvasCentre.getY();

        double offsetX = canvasX - viewportCentreX;
        double offsetY = canvasY - viewportCentreY;

        canvas.setTranslateX(canvas.getTranslateX() - offsetX);
        canvas.setTranslateY(canvas.getTranslateY() - offsetY);
    }

    private void applyZoom() {
        canvas.setScaleX(1 / this.zoom);
        canvas.setScaleY(1 / this.zoom);

        centreCanvas();
    }

    private void applyPanning(double deltaX, double deltaY) {
        double currentTranslateX = canvas.getTranslateX();
        double currentTranslateY = canvas.getTranslateY();

        // TODO: Set panning limit so that the user cannot pan further than the screen

        if (getScaledWidthOfCanvas() > this.getWidth()) canvas.setTranslateX(currentTranslateX + deltaX);
        if (getScaledHeightOfCanvas() > this.getHeight()) canvas.setTranslateY(currentTranslateY + deltaY);
    }
}
