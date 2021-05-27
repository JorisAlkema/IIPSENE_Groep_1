package Model;

// A City represents a city on the map, meaning it has a name, location (x/y offset) and size (radius)
// Should we choose to implement trainstation, this class would be a logical place to keep track of them
public class City {
    private final String name;
    private double xOffset;
    private double yOffset;
    // boolean trainstation?

    public City(String name, double xOffset, double yOffset) {
        this.name = name;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public String getName() {
        return name;
    }

    public double getxOffset() {
        return xOffset;
    }

    public double getyOffset() {
        return yOffset;
    }
}
