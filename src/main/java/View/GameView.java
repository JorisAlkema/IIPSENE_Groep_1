package View;

import App.MainState;
import Controller.MapController;
import Controller.GameController;
import Model.MapModel;
import Service.Observable;
import Service.Observer;
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

public class GameView extends BorderPane implements Observer {

    Label timerLabel;
    Label playerLabel;
    GameController gameController;

    public GameView() {
        gameController = new GameController(this);
        MapView mapView = new MapView();

        // Top pane
        timerLabel = new Label("0:00");
        setAlignment(timerLabel, Pos.CENTER);
        timerLabel.setStyle(    "-fx-font-family: Merriweather;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: 30;");
        setTop(timerLabel);

        // Center pane
        setCenter(mapView);

        // Left pane
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30));
        Image zoomInImage = new Image("icons/button_zoom_in.png");
        Image zoomOutImage = new Image("icons/button_zoom_out.png");
        ImageView imageView = new ImageView(zoomInImage);

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (mapView.getMapController().getMapModel().isZoomedIn()) {
                mapView.getMapController().zoomOut();
                imageView.setImage(zoomInImage);
            } else {
                mapView.getMapController().zoomIn();
                imageView.setImage(zoomOutImage);
            }
        });

        Button mainmenu = new Button("Return to menu");
        mainmenu.setOnAction(e -> {
            Scene newScene = new Scene(new MainMenuView());
            String css = "css/styling.css";
            newScene.getStylesheets().add(css);
            MainState.primaryStage.setScene(newScene);
        });

        playerLabel = new Label("Current player: ");

        vBox.getChildren().addAll(imageView, mainmenu, playerLabel);

        setLeft(vBox);
        setRight(new CardView());

        gameController.registerObserver(this);
        gameController.setTimerText(gameController.getTimer());
    }

    @Override
    public void update(Observable observable, Object object, String type) {
        if (type.equals("timer")) {
            System.out.println("Timer updated!" + java.time.LocalTime.now());
            timerLabel.setText((String) object);
        } else if (type.equals("playername")) {
            System.out.println("Player text updated!");
            playerLabel.setText("Current player: " + object.toString());
        }

    }
}
