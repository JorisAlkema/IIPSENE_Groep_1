package View;

import Controller.MapController;
import Observers.MapObserver;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Constructs a scene with a pannable Map background.
 */
public class MapView extends ScrollPane implements MapObserver {
    private final MapController mapController;
    private StackPane stackPane;

    public MapView() {
        super();
        this.mapController = MapController.getInstance();
        this.mapController.registerObserver(this);
        this.stackPane = new StackPane();
        this.stackPane.getChildren().add(this.mapController.getMapModel().getBackgroundImage());
        this.setContent(initStackPane());
        // Hide scrollbars
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.setStyle("-fx-focus-color: transparent;");
    }

    private StackPane initStackPane() {
        // Stack overlays on top of the background image
        stackPane = new StackPane();
        stackPane.getChildren().add(mapController.getMapModel().getBackgroundImage());

        // Add city and routeCell overlays
        stackPane.getChildren().addAll(mapController.getMapModel().getCityOverlays());
        stackPane.getChildren().addAll(mapController.getMapModel().getRouteCellOverlays());

        return stackPane;
    }

    public void setBackgroundImage(ImageView backgroundImage) {
        this.stackPane.getChildren().set(0, backgroundImage);
    }

    public MapController getMapController() {
        return mapController;
    }

    @Override
    public void update(boolean zoomedIn, ImageView backgroundImage) {
        setPannable(zoomedIn);
        setBackgroundImage(backgroundImage);
        if (zoomedIn) {
            layout();
            setHvalue(getHmin() + (getHmax() - getHmin()) / 2);
            setVvalue(getVmin() + (getVmax() - getVmin()) / 2);
        }
    }
}
