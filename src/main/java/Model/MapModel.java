package Model;

import Controller.MapController;
import Service.GameSetupService;
import Service.Observable;
import Service.Observer;
import View.MapView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MapModel implements Observable {

    private ArrayList<Observer> observers;
    private final ArrayList<Route> routes;
    private final ArrayList<City> cities;
//    private final GameSetupService gameSetupService;


//    private ImageView backgroundImage;
    private ArrayList<RouteCell> routeCellOverlays;
    private final ArrayList<Circle> cityOverlays;
//    private StackPane stackPane;

    private boolean zoomedIn;
    private ImageView backgroundImage;
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


    public MapModel(ArrayList<Route> routes, ArrayList<City> cities) {
        this.observers = new ArrayList<>();
        this.cities = cities;
        this.routes = routes;
        this.cityOverlays = createCityOverlays();
        this.routeCellOverlays = createRouteCellOverlays();
        this.zoomedIn = false;

    }

    public void zoomIn() {
        if (zoomedIn) {
            return;
        }

        this.backgroundImage = bigBackgroundImage;
        for (Route route : routes) {
            for (RouteCell routeCell : route.getRouteCells()) {
                routeCell.setTranslateX(routeCell.getTranslateX() * 2);
                routeCell.setTranslateY(routeCell.getTranslateY() * 2);
            }
        }

        for (City city : cities) {
            city.setOffsetX(city.getOffsetX() * 2);
            city.setOffsetY(city.getOffsetY() * 2);
        }

        zoomedIn = true;
        notifyAllObservers(this.backgroundImage);
    }

    public void zoomOut() {
        if ( ! zoomedIn) {
            return;
        }

        this.backgroundImage = smallBackgroundImage;
        for (Route route : routes) {
            for (RouteCell routeCell : route.getRouteCells()) {
                routeCell.setTranslateX(routeCell.getTranslateX() / 2);
                routeCell.setTranslateY(routeCell.getTranslateY() / 2);
            }
        }

        for (City city : cities) {
            city.setOffsetX(city.getOffsetX() / 2);
            city.setOffsetY(city.getOffsetY() / 2);
        }

        zoomedIn = false;
    }

    private ArrayList<Circle> createCityOverlays() {
        ArrayList<Circle> circleList = new ArrayList<>();
        for (City city : this.cities) {
            Circle circle = new Circle();
            circle.setTranslateX(city.getOffsetX() / 2);
            circle.setTranslateY(city.getOffsetY() / 2);
            circle.setFill(Color.TRANSPARENT);
            circleList.add(circle);
        }
        return circleList;
    }

    private ArrayList<RouteCell> createRouteCellOverlays() {
        ArrayList<RouteCell> overlays = new ArrayList<>();

        for (Route route : routes) {
            for (RouteCell routeCell : route.getRouteCells()) {
                routeCell.setTranslateX(routeCell.getTranslateX() / 2);
                routeCell.setTranslateY(routeCell.getTranslateY() / 2);
                routeCell.setFill(Color.TRANSPARENT);
                overlays.add( routeCell );
            }
        }
        return overlays;
    }

    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {

    }

    @Override
    public void notifyAllObservers(Object o) {
        for(Observer observer : this.observers) {
            observer.update(this, o);
        }
    }

    public ImageView getBackgroundImage() {
        return backgroundImage;
    }

    public ArrayList<Circle> getCityOverlays() {
        return cityOverlays;
    }

    public ArrayList<RouteCell> getRouteCellOverlays() {
        return routeCellOverlays;
    }

    //    public void zoomIn() {
//        if (zoomedIn) {
//            return;
//        }
//        this.backgroundImage = bigBackgroundImage;
//        stackPane.getChildren().set(0, this.backgroundImage);
//        setPannable(true);
//        for (Rectangle rectangle : rectangleOverlays) {
//            rectangle.setWidth(bigCellWidth);
//            rectangle.setHeight(bigCellHeight);
//            rectangle.setTranslateX(rectangle.getTranslateX() * 2);
//            rectangle.setTranslateY(rectangle.getTranslateY() * 2);
//            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
//                rectangle.setFill(bigImagePattern);
//            }
//        }
//        for (Circle circle : circleOverlays) {
//            circle.setRadius(bigRadius);
//            circle.setTranslateX(circle.getTranslateX() * 2);
//            circle.setTranslateY(circle.getTranslateY() * 2);
//        }
//        this.layout();
//        setHvalue(getHmin() + (getHmax() - getHmin()) / 2);
//        setVvalue(getVmin() + (getVmax() - getVmin()) / 2);
//        zoomedIn = true;
//    }
//
//    public void zoomOut() {
//        if (! zoomedIn) {
//            return;
//        }
//        this.backgroundImage = smallBackgroundImage;
//        stackPane.getChildren().set(0, this.backgroundImage);
//        setPannable(false);
//        for (Rectangle rectangle : rectangleOverlays) {
//            rectangle.setWidth(smallCellWidth);
//            rectangle.setHeight(smallCellHeight);
//            rectangle.setTranslateX(rectangle.getTranslateX() / 2);
//            rectangle.setTranslateY(rectangle.getTranslateY() / 2);
//            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
//                rectangle.setFill(smallImagePattern);
//            }
//        }
//        for (Circle circle : circleOverlays) {
//            circle.setRadius(smallRadius);
//            circle.setTranslateX(circle.getTranslateX() / 2);
//            circle.setTranslateY(circle.getTranslateY() / 2);
//        }
//        zoomedIn = false;
//    }
}
