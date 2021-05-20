package Model;

// RouteCell represents a single cell on the map. It is part of a Route.
// A RouteCell has no color or type of its own, this is determined in the Route class.
// The location of the RouteCell on the map is determined by its xOffset, yOffset and rotation.
public class RouteCell {
    private double xOffset;
    private double yOffset;
    private final double rotation;

    public RouteCell(double xOffset, double yOffset, double rotation) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.rotation = rotation;
    }
}
