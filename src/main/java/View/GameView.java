package View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView extends BorderPane {
    public GameView(Stage primaryStage) {
        MapView mapView = new MapView();

        // Center pane
        setCenter(mapView);

        // Left pane
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30));
        Image zoomInImage = new Image("icons/button_zoom_in.png");
        Image zoomOutImage = new Image("icons/button_zoom_out.png");
        ImageView imageView = new ImageView(zoomInImage);

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (mapView.isZoomedIn()) {
                mapView.zoomOut();
                imageView.setImage(zoomInImage);
            } else {
                mapView.zoomIn();
                imageView.setImage(zoomOutImage);
            }
        });

        Button mainmenu = new Button("Return to menu");
        mainmenu.setOnAction(e -> {
            Scene newScene = new Scene(new MainMenuView(primaryStage), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
            String css = "css/styling.css";
            newScene.getStylesheets().add(css);
            primaryStage.setScene(newScene);
        });

        vBox.getChildren().addAll(imageView,mainmenu);

        setLeft(vBox);
        setRight(new CardView());
    }
}
