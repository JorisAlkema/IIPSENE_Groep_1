package Model;

// A City represents a city on the map, meaning it has a name, location (x/y offset) and size (radius)
// Should we choose to implement trainstation, this class would be a logical place to keep track of them
public class City {
    private final String name;
    private double offsetX;
    private double offsetY;
    // boolean trainstation?

    public City(String name, double offsetX, double offsetY) {
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public String getName() {
        return name;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }
}
