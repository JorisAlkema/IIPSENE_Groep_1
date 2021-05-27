package View;

import Model.GameInfo;
import Model.Observer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView extends BorderPane implements Observer {

    Label label;
    GameInfo gameInfo;

    public GameView(Stage primaryStage) {
        gameInfo = new GameInfo(primaryStage);
        MapView mapView = new MapView();

        // Top pane
        label = new Label("0:00");
        setAlignment(label, Pos.CENTER);
        label.setStyle("-fx-font-family: Merriweather;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 30;");
        setTop(label);

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

        gameInfo.addObserver(this);
        gameInfo.countdownTimer();
        gameInfo.setTimerText(gameInfo.getTimer());
    }

    @Override
    public void update(Object timerText) {
        label.setText((String) timerText);
    }
}
