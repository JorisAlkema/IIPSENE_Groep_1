package Controller;

import Model.City;
import Model.MapModel;
import Model.Route;
import Model.RouteCell;
import Service.GameSetupService;
import View.MapView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class MapController {
    private final MapModel mapModel;
    private final MapView mapView;
    private final GameSetupService gameSetupService;


    public MapController(MapView mapView) {
        this.gameSetupService = new GameSetupService();
        this.mapView = mapView;
        this.mapModel = new MapModel(this, gameSetupService.getRoutes(), gameSetupService.getCities());
        this.mapModel.setCityOverlays(createCityOverlays());
        this.mapModel.setRouteCellOverlays(createRouteCellOverlays());
    }

    private ArrayList<Circle> createCityOverlays() {
        ArrayList<Circle> circleList = new ArrayList<>();
        for (City city : this.mapModel.getCities()) {
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
        for (Route route : this.mapModel.getRoutes()) {
            for (RouteCell routeCell : route.getRouteCells()) {
                routeCell.setTranslateX(routeCell.getTranslateX() / 2);
                routeCell.setTranslateY(routeCell.getTranslateY() / 2);
                routeCell.setFill(Color.TRANSPARENT);
                overlays.add( routeCell );
            }
        }
        return overlays;
    }

    public void zoomIn() {
        if (this.mapModel.isZoomedIn()) {
            return;
        }
        this.mapModel.setZoomedIn(true);
        this.mapView.setPannable(true);
        this.mapView.setBackgroundImage(this.mapModel.getBackgroundImage());

        for (Rectangle rectangle : this.mapModel.getRouteCellOverlays()) {
            rectangle.setWidth(this.mapModel.getCellWidth());
            rectangle.setHeight(this.mapModel.getCellHeight());
            rectangle.setTranslateX(rectangle.getTranslateX() * 2);
            rectangle.setTranslateY(rectangle.getTranslateY() * 2);
            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
                rectangle.setFill(this.mapModel.getBigImagePattern());
            }
        }

        for (Circle circle : this.mapModel.getCityOverlays()) {
            circle.setRadius(this.mapModel.getBigRadius());
            circle.setTranslateX(circle.getTranslateX() * 2);
            circle.setTranslateY(circle.getTranslateY() * 2);
        }
        this.mapView.layout();
        this.mapView.setHvalue(this.mapView.getHmin() + (this.mapView.getHmax() - this.mapView.getHmin()) / 2);
        this.mapView.setVvalue(this.mapView.getVmin() + (this.mapView.getVmax() - this.mapView.getVmin()) / 2);
    }

    public void zoomOut() {
        if (! this.mapModel.isZoomedIn()) {
            return;
        }
        this.mapModel.setZoomedIn(false);
        this.mapView.setPannable(false);
        this.mapView.setBackgroundImage(this.mapModel.getBackgroundImage());

        for (Rectangle rectangle : this.mapModel.getRouteCellOverlays()) {
            rectangle.setWidth(this.mapModel.getCellWidth());
            rectangle.setHeight(this.mapModel.getCellHeight());
            rectangle.setTranslateX(rectangle.getTranslateX() / 2);
            rectangle.setTranslateY(rectangle.getTranslateY() / 2);
            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
                rectangle.setFill(this.mapModel.getSmallImagePattern());
            }
        }

        for (Circle circle : this.mapModel.getCityOverlays()) {
            circle.setRadius(this.mapModel.getSmallRadius());
            circle.setTranslateX(circle.getTranslateX() / 2);
            circle.setTranslateY(circle.getTranslateY() / 2);
        }
    }

    public MapModel getMapModel() {
        return mapModel;
    }
}
