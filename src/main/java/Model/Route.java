package Model;

import java.util.ArrayList;
import java.util.Objects;

// Route represents a single route on the map, connecting two Cities with a
// given amount of RouteCells of a given color. It can be claimed by a Player.
public class Route {
    private City firstCity;
    private City secondCity;
    private ArrayList<RouteCell> routeCells;
    private String color;
    private String type; // STANDARD, TUNNEL or FERRY
    private int requiredLocomotives; // 0 if not of type FERRY

    public Route(City firstCity, City secondCity, ArrayList<RouteCell> routeCells, String color, String type, int requiredLocomotives) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.routeCells = routeCells;
        this.color = color;
        this.type = type;
        this.requiredLocomotives = requiredLocomotives;
    }

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
            return  false;
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
