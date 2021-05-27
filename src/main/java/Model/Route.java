package Model;

import java.util.ArrayList;

// Route represents a single route on the map, connecting two Cities with a
// given amount of RouteCells of a given color. It can be claimed by a Player.
public class Route {
    private final City firstCity;
    private final City secondCity;
    private final ArrayList<RouteCell> routeCells;
    private final String color;
    private final String type; // STANDARD, TUNNEL or FERRY
    private final int locomotives; // 0 if not of type FERRY
//    private Player owner;

    public Route(City firstCity, City secondCity, ArrayList<RouteCell> routeCells, String color, String type, int locomotives) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.routeCells = routeCells;
        this.color = color;
        this.type = type;
        this.locomotives = locomotives;
    }

    public ArrayList<RouteCell> getRouteCells() {
        return routeCells;
    }
}
