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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameView extends BorderPane implements Observer {
    Label timerLabel;
    Label currentPlayerLabel;
    GameController gameController;

    public GameView() {
        // Init
        gameController = new GameController(this);

        // Top pane
        timerLabel = new Label("0:00");
        setAlignment(timerLabel, Pos.CENTER);
        timerLabel.setStyle(    "-fx-font-family: Merriweather;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: 30;");
        setTop(timerLabel);

        // Left pane
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30));
        Image zoomInImage = new Image("icons/button_zoom_in.png");
        Image zoomOutImage = new Image("icons/button_zoom_out.png");
        ImageView mapZoomButton = new ImageView(zoomInImage);

        Button mainmenuButton = new Button("Return to menu");
        mainmenuButton.setOnAction(e -> {
            Scene newScene = new Scene(new MainMenuView());
            String css = "css/styling.css";
            newScene.getStylesheets().add(css);
            MainState.primaryStage.setScene(newScene);
        });

        currentPlayerLabel = new Label("Current player: ");

        vBox.getChildren().addAll(mapZoomButton, mainmenuButton, currentPlayerLabel);

        for (StackPane stackPane : gameController.createOpponentViews()) {
            vBox.getChildren().add(stackPane);
        }

        setLeft(vBox);

        // Center pane
        MapView mapView = new MapView();
        mapView.getMapController().setGameController(gameController);
        setCenter(mapView);

        mapZoomButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (mapView.getMapController().getMapModel().isZoomedIn()) {
                mapView.getMapController().zoomOut();
                mapZoomButton.setImage(zoomInImage);
            } else {
                mapView.getMapController().zoomIn();
                mapZoomButton.setImage(zoomOutImage);
            }
        });

        // Right Pane
        setRight(new CardView(gameController));

        // Bottom pane
        setBottom(new HandView());

        //
        gameController.registerObserver(this);
        gameController.setTimerText(gameController.getTimer());
    }

    @Override
    public void update(Observable observable, Object object, String type) {
        if (type.equals("timer")) {
            timerLabel.setText((String) object);
        } else if (type.equals("playername")) {
            currentPlayerLabel.setText("Current player: " + object.toString());
        }

    }
}
