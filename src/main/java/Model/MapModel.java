package Model;

import Controller.MapController;
import Service.Observable;
import Service.Observer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import java.util.ArrayList;

public class MapModel implements Observable {
    private final MapController mapController;
    private ArrayList<Observer> observers;

    private final ArrayList<Route> routes;
    private final ArrayList<City> cities;
    private ArrayList<RouteCell> routeCellOverlays;
    private ArrayList<Circle> cityOverlays;
    private boolean zoomedIn;
    private final ImageView bigBackgroundImage = new ImageView("maps/map_big.jpg");
    private final ImageView smallBackgroundImage = new ImageView("maps/map_small.jpg");
    private final ImagePattern smallImagePattern = new ImagePattern(new Image("icons/train_small.png"));
    private final ImagePattern bigImagePattern = new ImagePattern(new Image("icons/train.png"));
    private static final double smallCellWidth = 35;
    private static final double smallCellHeight = 12;
    private static final double bigCellWidth = 70;
    private static final double bigCellHeight = 23;
    private static final double smallRadius = 7;
    private static final double bigRadius = 15;

    public MapModel(MapController mapController, ArrayList<Route> routes, ArrayList<City> cities) {
        this.mapController = mapController;
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

    public ImagePattern getImagePattern() {
        return (zoomedIn ? bigImagePattern : smallImagePattern);
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

    public ArrayList<RouteCell> getRouteCellOverlays() {
        return routeCellOverlays;
    }

    public void setCityOverlays(ArrayList<Circle> cityOverlays) {
        this.cityOverlays = cityOverlays;
    }

    public void setRouteCellOverlays(ArrayList<RouteCell> routeCellOverlays) {
        this.routeCellOverlays = routeCellOverlays;
    }

    public boolean isZoomedIn() {
        return zoomedIn;
    }

    public void setZoomedIn(boolean zoomedIn) {
        this.zoomedIn = zoomedIn;
    }

    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {

    }

    @Override
    public void notifyAllObservers(Object o, String type) {
        for(Observer observer : this.observers) {
            observer.update(this, o, type);
        }
    }
}
