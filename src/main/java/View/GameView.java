package View;

import App.MainState;
import Controller.GameController;
import Model.Player;
import Model.PlayerTurn;
import Model.TrainCard;
import Observers.CardsObserver;
import Observers.PlayerTurnObverser;
import Observers.TurnTimerObserver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class GameView extends StackPane implements TurnTimerObserver, CardsObserver, PlayerTurnObverser {
    private Label timerLabel;
    private Label currentPlayerLabel;
    private BorderPane borderPane;
    private VBox cardsBox;
    private MapView mapView;
    private GameController gameController;

    public GameView() {
        // Init
        gameController = new GameController();
        gameController.registerTurnTimerObserver(this);
        gameController.registerCardsObserver(this);
        gameController.registerPlayerTurnObserver(this);

        borderPane = new BorderPane();
        initLeftPane();
        initCenterPane();
        initRightPane();
        initBottomPane();

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        ImageView background = new ImageView("images/bg-splash.jpg");
        background.setFitWidth(MainState.WINDOW_WIDTH);
        background.setFitHeight(MainState.WINDOW_HEIGHT);
        background.setEffect(colorAdjust);

        this.getChildren().addAll(background, borderPane);
    }

    private void initLeftPane() {
        Image zoomInImage = new Image("icons/button_zoom_in.png");
        Image zoomOutImage = new Image("icons/button_zoom_out.png");
        ImageView mapZoomButton = new ImageView(zoomInImage);
        mapZoomButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (mapView.getMapController().getMapModel().isZoomedIn()) {
                mapView.getMapController().zoomOut();
                mapZoomButton.setImage(zoomInImage);
            } else {
                mapView.getMapController().zoomIn();
                mapZoomButton.setImage(zoomOutImage);
            }
        });
        timerLabel = new Label("0:00");
        timerLabel.setId("timerLabel");
        currentPlayerLabel = new Label();

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(timerLabel, mapZoomButton, currentPlayerLabel);
        for (StackPane stackPane : gameController.createOpponentViews()) {
            vBox.getChildren().add(stackPane);
        }
        borderPane.setLeft(vBox);
    }

    private void initCenterPane() {
        mapView = new MapView();
        mapView.getMapController().setGameController(gameController);
        borderPane.setCenter(mapView);
    }

    private void initRightPane() {
        cardsBox = new VBox();
        borderPane.setRight(cardsBox);
    }

    private void initBottomPane() {
        HandView handView = new HandView();
        borderPane.setBottom(handView);
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

    @Override
    public void update(PlayerTurn playerTurn) {
        currentPlayerLabel.setText("Current player: " + playerTurn.getCurrentPlayerUsername());
    }
}
