package View;

import App.MainState;
import Controller.DestinationTicketController;
import Controller.GameController;
import Model.DestinationTicketDeck;
import Observers.TimerObserver;
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

public class GameView extends BorderPane implements TimerObserver {
    Label timerLabel;
    GameController gameController;

    public GameView() {
        // Init
        gameController = new GameController(this);

        // Top pane
        timerLabel = new Label("0:00");
        setAlignment(timerLabel, Pos.CENTER);
        timerLabel.setId("timerLabel");
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
            String css = "css/mainMenuStyle.css";
            newScene.getStylesheets().add(css);
            MainState.primaryStage.setScene(newScene);
        });

        vBox.getChildren().addAll(mapZoomButton, mainmenuButton);

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
        DestinationTicketController destinationTicketController = new DestinationTicketController(mapView.getMapController().getGameSetupService().getDestinationTickets());
        DestinationPopup.showPopUp(destinationTicketController.getDestinationTickets(true));
//        gameController.setTimerText(gameController.getTimer());
    }

    @Override
    public void update(String timerText) {
            timerLabel.setText(timerText);
    }
}
