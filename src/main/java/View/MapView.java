package View;

import Controller.MapController;
import Model.MapModel;
import Model.RouteCell;
import Service.GameSetupService;
import Service.Observable;
import Service.Observer;
import Service.OverlayEventHandler;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/** Constructs a scene with a pannable Map background. */
public class MapView extends ScrollPane implements Observer {
    private final MapController mapController;
//    private final GameSetupService gameSetupService;
    private ImageView backgroundImage;
    private ArrayList<RouteCell> routeCellOverlays;
    private ArrayList<Circle> circleOverlays;
    private StackPane stackPane;
    private boolean zoomedIn;
    private final ImageView bigBackgroundImage = new ImageView("maps/map_big.jpg");
    private final ImageView smallBackgroundImage = new ImageView("maps/map_small.jpg");
    private final ImagePattern smallImagePattern = new ImagePattern(new Image("icons/train_small.png"));
    private final ImagePattern bigImagePattern = new ImagePattern(new Image("icons/train.png"));
    private static final double smallCellWidth = 35; // 70x23 for large, 35x12 for small
    private static final double smallCellHeight = 12;
    private static final double bigCellWidth = 70;
    private static final double bigCellHeight = 23;
    private static final double smallRadius = 7;
    private static final double bigRadius = 15;

    public MapView(MapController mapController) {
        super();
        this.mapController = mapController;
        this.mapController.getMapModel().registerObserver(this);
//        this.gameSetupService = new GameSetupService();
        this.backgroundImage = smallBackgroundImage;
        this.setContent(initStackPane());
        // Hide scrollbars
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.zoomedIn = false;
//        MapModel model = new MapModel();
//        model.registerObserver(this);
    }

    public void zoomIn() {
        if (zoomedIn) {
            return;
        }
        this.backgroundImage = bigBackgroundImage;
        stackPane.getChildren().set(0, this.backgroundImage);
        setPannable(true);
        for (Rectangle rectangle : routeCellOverlays) {
            rectangle.setWidth(bigCellWidth);
            rectangle.setHeight(bigCellHeight);
            rectangle.setTranslateX(rectangle.getTranslateX() * 2);
            rectangle.setTranslateY(rectangle.getTranslateY() * 2);
            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
                rectangle.setFill(bigImagePattern);
            }
        }
        for (Circle circle : circleOverlays) {
            circle.setRadius(bigRadius);
            circle.setTranslateX(circle.getTranslateX() * 2);
            circle.setTranslateY(circle.getTranslateY() * 2);
        }
        this.layout();
        setHvalue(getHmin() + (getHmax() - getHmin()) / 2);
        setVvalue(getVmin() + (getVmax() - getVmin()) / 2);
        zoomedIn = true;
    }

    public void zoomOut() {
        if (! zoomedIn) {
            return;
        }
        this.backgroundImage = smallBackgroundImage;
        stackPane.getChildren().set(0, this.backgroundImage);
        setPannable(false);
        for (Rectangle rectangle : routeCellOverlays) {
            rectangle.setWidth(smallCellWidth);
            rectangle.setHeight(smallCellHeight);
            rectangle.setTranslateX(rectangle.getTranslateX() / 2);
            rectangle.setTranslateY(rectangle.getTranslateY() / 2);
            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
                rectangle.setFill(smallImagePattern);
            }
        }
        for (Circle circle : circleOverlays) {
            circle.setRadius(smallRadius);
            circle.setTranslateX(circle.getTranslateX() / 2);
            circle.setTranslateY(circle.getTranslateY() / 2);
        }
        zoomedIn = false;
    }

    private StackPane initStackPane() {
        // Stack overlays on top of the background image
        stackPane = new StackPane();
        stackPane.getChildren().add(backgroundImage);

        circleOverlays = mapController.getMapModel().getCityOverlays();
        for (Circle circle : circleOverlays) {
            circle.setRadius(smallRadius);
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
        }
        stackPane.getChildren().addAll(circleOverlays);

        routeCellOverlays = mapController.getMapModel().getRouteCellOverlays();
        for (RouteCell routeCellOverlay : routeCellOverlays) {
            routeCellOverlay.setWidth(smallCellWidth);
            routeCellOverlay.setHeight(smallCellHeight);
            routeCellOverlay.addEventHandler(MouseEvent.ANY, new OverlayEventHandler(
                    e -> {
                        if (routeCellOverlay.getFill().equals(Color.TRANSPARENT)) {
                            for (RouteCell routeCell : routeCellOverlay.getParentRoute().getRouteCells()) {
                                routeCell.setFill(zoomedIn ? bigImagePattern : smallImagePattern);
                            }
                        } else {
                            for (RouteCell routeCell : routeCellOverlay.getParentRoute().getRouteCells()) {
                                routeCell.setFill(Color.TRANSPARENT);
                            }
                        }
                    },
                    Event::consume)
            );
        }
        stackPane.getChildren().addAll(routeCellOverlays);

        return stackPane;
    }


    @Override
    public void update(Observable observable, Object o) {
        this.backgroundImage = ((MapModel) observable).getBackgroundImage();
        this.zoomedIn = ((MapModel) observable).isZoomedIn();
    }

    public boolean isZoomedIn() {
        return zoomedIn;
    }
}
