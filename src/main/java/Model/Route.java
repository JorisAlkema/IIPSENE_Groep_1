package Model;

import java.util.ArrayList;

// Route represents a single route on the map, connecting two Cities with a
// given amount of RouteCells of a given color. It can be claimed by a Player.
public class Route {
    private final ArrayList<City> cities;
    private final ArrayList<RouteCell> routeCells;
    private final String color;
    private final String type;
    private Player owner;

    public Route(ArrayList<City> cities, ArrayList<RouteCell> routeCells, String color, String type, Player owner) {
        this.cities = cities;
        this.routeCells = routeCells;
        this.color = color;
        this.type = type;
        this.owner = owner;
    }

    public Route(ArrayList<City> cities, ArrayList<RouteCell> routeCells, String color, String type) {
        this.cities = cities;
        this.routeCells = routeCells;
        this.color = color;
        this.type = type;
    }
}
