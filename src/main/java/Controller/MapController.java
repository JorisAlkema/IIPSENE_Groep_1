package Controller;

import Model.City;
import Model.MapModel;
import Model.Route;
import Model.RouteCell;
import Service.GameSetupService;
import Service.OverlayEventHandler;
import View.MapView;
import javafx.event.Event;
import javafx.scene.input.MouseEvent;
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
        this.mapModel = new MapModel(gameSetupService.getRoutes(), gameSetupService.getCities());
        this.mapModel.setCityOverlays(createCityOverlays());
        this.mapModel.setRouteCellOverlays(createRouteCellOverlays());
    }

    /**
     * Create the overlays for the cities on the map.
     * Sets the correct location, size and adds event handler
     * @return A list of Circle objects which represent the overlays
     */
    private ArrayList<Circle> createCityOverlays() {
        ArrayList<Circle> circleList = new ArrayList<>();
        for (City city : this.mapModel.getCities()) {
            Circle circle = new Circle();
            circle.setTranslateX(city.getOffsetX() / 2);
            circle.setTranslateY(city.getOffsetY() / 2);
            circle.setRadius(this.mapModel.getRadius());
            circle.setFill(Color.TRANSPARENT);
            circle.addEventHandler(MouseEvent.ANY, new OverlayEventHandler(
                    e -> {
                        if (circle.getFill().equals(Color.TRANSPARENT)) {
                            circle.setFill( Color.BLACK);
                        } else {
                            circle.setFill(Color.TRANSPARENT);
                        }
                    },
                    Event::consume)
            );
            circleList.add(circle);
        }
        return circleList;
    }

    /**
     * Creates the overlays for the individual routecells on the map.
     * Sets the correct location, size, rotation and adds event handler
     * @return A list of RouteCell object which represent the overlays
     */
    private ArrayList<RouteCell> createRouteCellOverlays() {
        ArrayList<RouteCell> overlays = new ArrayList<>();
        for (Route route : this.mapModel.getRoutes()) {
            for (RouteCell routeCell : route.getRouteCells()) {
                routeCell.setTranslateX(routeCell.getTranslateX() / 2);
                routeCell.setTranslateY(routeCell.getTranslateY() / 2);
                routeCell.setWidth(this.mapModel.getCellWidth());
                routeCell.setHeight(this.mapModel.getCellHeight());
                routeCell.addEventHandler(MouseEvent.ANY, new OverlayEventHandler(
                        e -> handleRouteCellClickEvent(routeCell),
                        Event::consume
                ));
                routeCell.setFill(Color.TRANSPARENT);
                overlays.add( routeCell );
            }
        }
        return overlays;
    }

    /**
     * Handles click event for when the user clicks on a RouteCell on the mapView
     * @param routeCell The routeCell overlay that was clicked on
     */
    public void handleRouteCellClickEvent(RouteCell routeCell) {
        if (routeCell.getFill().equals(Color.TRANSPARENT)) {
            for (RouteCell cellInSameRoute : routeCell.getParentRoute().getRouteCells()) {
                cellInSameRoute.setFill(this.mapModel.getImagePattern());
            }
        } else {
            for (RouteCell cellInSameRoute : routeCell.getParentRoute().getRouteCells()) {
                cellInSameRoute.setFill(Color.TRANSPARENT);
            }
        }
    }

    /**
     * Zooms in on the mapModel and updates the mapView
     */
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
                rectangle.setFill(this.mapModel.getImagePattern());
            }
        }

        for (Circle circle : this.mapModel.getCityOverlays()) {
            circle.setRadius(this.mapModel.getRadius());
            circle.setTranslateX(circle.getTranslateX() * 2);
            circle.setTranslateY(circle.getTranslateY() * 2);
        }
        this.mapView.layout();
        this.mapView.setHvalue(this.mapView.getHmin() + (this.mapView.getHmax() - this.mapView.getHmin()) / 2);
        this.mapView.setVvalue(this.mapView.getVmin() + (this.mapView.getVmax() - this.mapView.getVmin()) / 2);
    }

    /**
     * Zooms out on the mapModel and updates the mapView
     */
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
                rectangle.setFill(this.mapModel.getImagePattern());
            }
        }

        for (Circle circle : this.mapModel.getCityOverlays()) {
            circle.setRadius(this.mapModel.getRadius());
            circle.setTranslateX(circle.getTranslateX() / 2);
            circle.setTranslateY(circle.getTranslateY() / 2);
        }
    }

    public MapModel getMapModel() {
        return mapModel;
    }
}
