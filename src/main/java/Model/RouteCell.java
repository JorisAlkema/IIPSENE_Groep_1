package Model;

import javafx.scene.shape.Rectangle;

// RouteCell represents a single cell on the map. It is part of a Route.
// A RouteCell has no color or type of its own, this is determined in the Route class.
// The location of the RouteCell on the map is determined by its offsetX, offsetY and rotation.
public class RouteCell extends Rectangle {

    private Route parentRoute;

    public RouteCell(double translateX, double translateY, double rotation) {
        super();
        setTranslateX(translateX);
        setTranslateY(translateY);
        setRotate(rotation);

    }

    public void setParentRoute(Route parentRoute) {
        this.parentRoute = parentRoute;
    }

    public Route getParentRoute() {
        return parentRoute;
    }
}
