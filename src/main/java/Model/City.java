package Model;

// A City represents a city on the map, meaning it has a name, location (x/y offset) and size (radius)
// Should we choose to implement trainstation, this class would be a logical place to keep track of them
public class City {
    private final String name;
    private double xOffset;
    private double yOffset;
    private double radius;
    // boolean trainstation?

    public City(String name, double xOffset, double yOffset, double radius) {
        this.name = name;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.radius = radius;
    }
}
