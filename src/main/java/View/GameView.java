package View;

import App.MainState;
import Controller.DestinationTicketController;
import Controller.GameController;
import Model.TrainCard;
import Observers.CardsObserver;
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

import java.util.ArrayList;

public class GameView extends BorderPane implements TimerObserver, CardsObserver {
    Label timerLabel;
    GameController gameController;

    VBox cardsBox = new VBox();

    public GameView() {
        // Init
        gameController = new GameController();

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

        // Closed and open Cards View
        cardsBox.setPadding(new Insets(0, 35, 0, 35));
        setRight(cardsBox);

        // Bottom pane
        setBottom(new HandView());

        //
        gameController.registerObserver(this);
        gameController.registerCardsObserver(this);

        // TODO: find more MVC-like way to pass initial list of tickets that should form the deck
        DestinationPopUp destinationPopUp = new DestinationPopUp(mapView.getMapController().getGameSetupService().getDestinationTickets());
        destinationPopUp.showAtStartOfGame();
//        gameController.setTimerText(gameController.getTimer());
    }

    @Override
    public void update(String timerText) {
        timerLabel.setText(timerText);
    }

    @Override
    public void update(ArrayList<TrainCard> openCards) {
        if (openCards != null) {
            final String CLOSED_CARD_PATH = "traincards/traincard_back_small_rotated.png";
            ImageView closedTrainCard = new ImageView(new Image(CLOSED_CARD_PATH));
            ArrayList<ImageView> openTrainCards = new ArrayList<>();

            for (TrainCard openCard : openCards) {
                String cardColor = openCard.getColor();
                String path = "traincards/traincard_" + cardColor + "_small_rotated.png";
                openTrainCards.add(new ImageView(new Image(path)));
            }

            // onClick events and ID
            closedTrainCard.setId("TrainCard");
            closedTrainCard.setOnMouseClicked(e -> {
                this.gameController.pickClosedCard();
            });

            for (ImageView openTrainCard : openTrainCards) {
                openTrainCard.setId("TrainCard");
                openTrainCard.setOnMouseClicked(e -> {
                    this.gameController.pickOpenCard(openTrainCards.indexOf(openTrainCard));
                });
            }

            cardsBox.getChildren().removeAll(cardsBox.getChildren());
            cardsBox.getChildren().add(closedTrainCard);
            cardsBox.getChildren().addAll(openTrainCards);
        }
    }
}
