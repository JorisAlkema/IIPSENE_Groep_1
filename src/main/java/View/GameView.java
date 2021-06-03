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

    Label label;
    GameController gameController;

    public GameView() {
        gameController = new GameController(this);
        MapView mapView = new MapView();
        mapView.getMapController().setGameController(gameController);

        // Top pane
        label = new Label("0:00");
        setAlignment(label, Pos.CENTER);
        label.setStyle( "-fx-font-family: Merriweather;" +
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

        vBox.getChildren().addAll(imageView,mainmenu);

        setLeft(vBox);
        
        // Bottom pane
        setBottom(new HandView());

        gameController.registerObserver(this);
        gameController.countdownTimer();
        gameController.setTimerText(gameController.getTimer());
    }

    @Override
    public void update(Observable observable, Object timerText) {
        label.setText((String) timerText);
    }
}
