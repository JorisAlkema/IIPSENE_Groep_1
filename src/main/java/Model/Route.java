package Model;

import java.util.ArrayList;
import java.util.Objects;

// Route represents a single route on the map, connecting two Cities with a
// given amount of RouteCells of a given color. It can be claimed by a Player.
public class Route {
    private final City firstCity;
    private final City secondCity;
    private final ArrayList<RouteCell> routeCells;
    private final String color;
    private final String type; // STANDARD, TUNNEL or FERRY
    private final int requiredLocomotives; // 0 if not of type FERRY
    private Player owner;

    public Route(City firstCity, City secondCity, ArrayList<RouteCell> routeCells, String color, String type, int requiredLocomotives) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.routeCells = routeCells;
        this.color = color;
        this.type = type;
        this.requiredLocomotives = requiredLocomotives;
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

    @Override
    public int hashCode() {
        return Objects.hash(firstCity, secondCity);
    }

    public ArrayList<RouteCell> getRouteCells() {
        return routeCells;
    }

    public City getFirstCity() {
        return firstCity;
    }

    public City getSecondCity() {
        return secondCity;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public int getRequiredLocomotives() {
        return requiredLocomotives;
    }

    public int getLength() {
        return routeCells.size();
    }
}
