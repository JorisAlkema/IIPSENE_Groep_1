package Model;

import java.util.Objects;

// RouteCell represents a single cell on the map. It is part of a Route.
// A RouteCell has no color or type of its own, this is determined in the Route class.
// The location of the RouteCell on the map is determined by its offsetX, offsetY and rotation.
public class RouteCell {

    private double translateX;
    private double translateY;
    private double rotation;

    public RouteCell(double translateX, double translateY, double rotation) {
        this.translateX = translateX;
        this.translateY = translateY;
        this.rotation = rotation;
    }

    public RouteCell() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof RouteCell) {
            RouteCell routeCell = (RouteCell) o;
            return translateX == routeCell.getTranslateX()
                    && translateY == routeCell.getTranslateY()
                    && rotation == routeCell.getRotation();
        }
        return false;
    }

    @Override // Needed for equals method
    public int hashCode() {
        return Objects.hash(translateX, translateY, rotation);
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public double getRotation() {
        return rotation;
    }

    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }

    public void setTranslateY(double translateY) {
        this.translateY = translateY;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
