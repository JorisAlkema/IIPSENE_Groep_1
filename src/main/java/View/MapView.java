package View;

import Service.GameSetupService;
import Service.OverlayEventHandler;
import javafx.event.Event;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/** Constructs a scene with a pannable Map background. */
public class MapView extends ScrollPane {
    private final GameSetupService gameSetupService;
    private ImageView backgroundImage;
    private ArrayList<Rectangle> rectangleOverlays;
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

    public MapView() {
        super();
        this.gameSetupService = new GameSetupService();
        this.backgroundImage = smallBackgroundImage;
        this.setContent(initStackPane());
        // Hide scrollbars
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.zoomedIn = false;
    }

    public void zoomIn() {
        if (zoomedIn) {
            return;
        }
        this.backgroundImage = bigBackgroundImage;
        stackPane.getChildren().set(0, this.backgroundImage);
        setPannable(true);
        for (Rectangle rectangle : rectangleOverlays) {
            rectangle.setWidth(bigCellWidth);
            rectangle.setHeight(bigCellHeight);
            rectangle.setTranslateX(rectangle.getTranslateX() * 2);
            rectangle.setTranslateY(rectangle.getTranslateY() * 2);
            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
                rectangle.setFill(bigImagePattern);
            }
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
        for (Rectangle rectangle : rectangleOverlays) {
            rectangle.setWidth(smallCellWidth);
            rectangle.setHeight(smallCellHeight);
            rectangle.setTranslateX(rectangle.getTranslateX() / 2);
            rectangle.setTranslateY(rectangle.getTranslateY() / 2);
            if ( ! rectangle.getFill().equals(Color.TRANSPARENT)) {
                rectangle.setFill(smallImagePattern);
            }
        }
        zoomedIn = false;
    }

    private StackPane initStackPane() {
        // Stack overlays on top of the background image
        stackPane = new StackPane();
        stackPane.getChildren().add(backgroundImage);
        rectangleOverlays = gameSetupService.createMapViewOverlays();
        for (Rectangle rectangle : rectangleOverlays) {
            rectangle.setWidth(smallCellWidth);
            rectangle.setHeight(smallCellHeight);
            rectangle.addEventHandler(MouseEvent.ANY, new OverlayEventHandler(
                    e -> {
                        if (rectangle.getFill().equals(Color.TRANSPARENT)) {
                            rectangle.setFill( zoomedIn ? bigImagePattern : smallImagePattern );
                        } else {
                            rectangle.setFill(Color.TRANSPARENT);
                        }
                    },
                    Event::consume)
            );
        }
        stackPane.getChildren().addAll(rectangleOverlays);
        return stackPane;
    }

    public boolean isZoomedIn() {
        return zoomedIn;
    }
}
