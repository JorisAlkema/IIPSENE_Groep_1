package Model;

import java.util.ArrayList;

// A City represents a city on the map, meaning it has a name, location (x/y offset) and size (radius)
// Should we choose to implement trainstation, this class would be a logical place to keep track of them
public class City {
    private String name;
    private double offsetX;
    private double offsetY;
    private ArrayList<City> neighborCities;
    private boolean visited; // Used in pathfinding algorithm to see if two cities are connected
    // boolean trainstation?

    public City(String name, double offsetX, double offsetY) {
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.neighborCities = new ArrayList<>();
        this.visited = false;
    }

    public City() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof City) {
            City city = (City) o;
            return this.name.equals(city.getName());
        }
        return false;
    }
//
//    @Override
//    public int hashCode() {
//        return getName() != null ? getName().hashCode() : 0;
//    }

    public void setNeighborCities(ArrayList<City> neighborCities) {
        this.neighborCities = neighborCities;
    }

    public ArrayList<City> getNeighborCities() {
        return neighborCities;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
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
