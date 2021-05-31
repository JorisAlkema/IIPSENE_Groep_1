package Model;

// RouteCell represents a single cell on the map. It is part of a Route.
// A RouteCell has no color or type of its own, this is determined in the Route class.
// The location of the RouteCell on the map is determined by its offsetX, offsetY and rotation.
public class RouteCell {
    private double offsetX;
    private double offsetY;
    private final double rotation;

    public RouteCell(double offsetX, double offsetY, double rotation) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.rotation = rotation;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getRotation() {
        return rotation;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

}
