package Model;

import Observers.MapObservable;
import Observers.MapObserver;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MapModel implements MapObservable {
    private final ArrayList<MapObserver> observers;
    private final ArrayList<Route> routes;
    private final ArrayList<City> cities;
    private ArrayList<Rectangle> routeCellOverlays;
    private ArrayList<Circle> cityOverlays;
    private boolean zoomedIn;
    private static final ImageView bigBackgroundImage = new ImageView("maps/map_big.jpg");
    private static final ImageView smallBackgroundImage = new ImageView("maps/map_small.jpg");
    private static final ImagePattern blueImagePattern = new ImagePattern(new Image("icons/train_blue.png"));
    private static final ImagePattern greenImagePattern = new ImagePattern(new Image("icons/train_green.png"));
    private static final ImagePattern purpleImagePattern = new ImagePattern(new Image("icons/train_purple.png"));
    private static final ImagePattern redImagePattern = new ImagePattern(new Image("icons/train_red.png"));
    private static final ImagePattern yellowImagePattern = new ImagePattern(new Image("icons/train_yellow.png"));
    private static final double smallCellWidth = 35;
    private static final double smallCellHeight = 12;
    private static final double bigCellWidth = 70;
    private static final double bigCellHeight = 23;
    private static final double smallRadius = 7;
    private static final double bigRadius = 15;

    public MapModel(ArrayList<Route> routes, ArrayList<City> cities) {
        this.observers = new ArrayList<>();
        this.cities = cities;
        this.routes = routes;
        this.zoomedIn = false;
    }

    public double getCellWidth() {
        return (zoomedIn ? bigCellWidth : smallCellWidth);
    }

    public double getCellHeight() {
        return (zoomedIn ? bigCellHeight : smallCellHeight);
    }

    public double getRadius() {
        return (zoomedIn ? bigRadius : smallRadius);
    }

    public ImagePattern getImagePattern(String color) {
        color = color.toUpperCase();
        switch (color) {
            case "BLUE": return blueImagePattern;
            case "GREEN": return greenImagePattern;
            case "PURPLE": return purpleImagePattern;
            case "RED": return redImagePattern;
            case "YELLOW": return yellowImagePattern;
        }
        return null;
    }

    public ImageView getBackgroundImage() {
        return (zoomedIn ? bigBackgroundImage : smallBackgroundImage);
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public ArrayList<Circle> getCityOverlays() {
        return cityOverlays;
    }

    public ArrayList<Rectangle> getRouteCellOverlays() {
        return routeCellOverlays;
    }

    public void setCityOverlays(ArrayList<Circle> cityOverlays) {
        this.cityOverlays = cityOverlays;
    }

    public void setRouteCellOverlays(ArrayList<Rectangle> routeCellOverlays) {
        this.routeCellOverlays = routeCellOverlays;
    }

    public boolean isZoomedIn() {
        return zoomedIn;
    }

    public void setZoomedIn(boolean zoomedIn) {
        this.zoomedIn = zoomedIn;
    }

    @Override
    public void registerObserver(MapObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(MapObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (MapObserver observer : this.observers) {
            observer.update(zoomedIn, getBackgroundImage());
        }
    }
}
