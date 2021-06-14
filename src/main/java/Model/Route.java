package Model;

import java.util.ArrayList;

public class Route {
    private City firstCity;
    private City secondCity;
    private ArrayList<RouteCell> routeCells;
    private String color;
    private String type;
    private int requiredLocomotives;

    public Route(City firstCity, City secondCity, ArrayList<RouteCell> routeCells, String color, String type, int requiredLocomotives) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.routeCells = routeCells;
        this.color = color;
        this.type = type;
        this.requiredLocomotives = requiredLocomotives;
    }

    // Allow Firebase to create a new instance of the player object with an empty constructor,
    // which will be filled in using reflection.
    public Route() {
    }

    @Override
    public String toString() {
        return "Route: " + firstCity + "-" + secondCity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (o instanceof Route) {
            Route route = (Route) o;
            return this.firstCity.equals(route.firstCity) && this.secondCity.equals(route.secondCity);
        }
        return false;
    }

    public int routeLength() {
        return getRouteCells().size();
    }

    public City getFirstCity() {
        return firstCity;
    }

    public void setFirstCity(City firstCity) {
        this.firstCity = firstCity;
    }

    public City getSecondCity() {
        return secondCity;
    }

    public void setSecondCity(City secondCity) {
        this.secondCity = secondCity;
    }

    public ArrayList<RouteCell> getRouteCells() {
        return routeCells;
    }

    public void setRouteCells(ArrayList<RouteCell> routeCells) {
        this.routeCells = routeCells;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRequiredLocomotives() {
        return requiredLocomotives;
    }

    public void setRequiredLocomotives(int requiredLocomotives) {
        this.requiredLocomotives = requiredLocomotives;
    }
}
