package View;

import App.MainState;
import Controller.GameController;
import Model.*;
import Observers.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

import static Controller.MainMenuController.openRules;

public class GameView extends StackPane implements TurnTimerObserver, CardsObserver, BannerObserver, SystemMessageObserver {
    private MapView mapView;

    private final GameController gameController;
    private final BorderPane borderPane;
    private VBox cardsBox;
    private VBox playerBanners;
    private Label timerLabel;
    private Label systemMessage;
    private int rowIndex;

    public GameView() {
        gameController = new GameController();
        gameController.registerObservers(this);

        borderPane = new BorderPane();
        initLeftPane();
        initCenterPane();
        initRightPane();
        initBottomPane();

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        ImageView background = new ImageView("images/backgrounds/bg-splash.jpg");
        background.setFitWidth(MainState.WINDOW_WIDTH);
        background.setFitHeight(MainState.WINDOW_HEIGHT);
        background.setEffect(colorAdjust);


        this.getChildren().addAll(background, borderPane);
    }

    private void initLeftPane() {
        Image zoomInImage = new Image("images/icons/button_zoom_in.png");
        Image zoomOutImage = new Image("images/icons/button_zoom_out.png");
        Image information = new Image("images/icons/button_game_info.png");
        ImageView mapZoomButton = new ImageView(zoomInImage);
        ImageView informationButton = new ImageView(information);

        informationButton.addEventFilter(MouseEvent.MOUSE_CLICKED,e ->{
            try {
                openRules();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

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
        timerLabel.setMinWidth(100);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10 ,10));
        vBox.setSpacing(10);

        systemMessage = new Label("");
        systemMessage.setId("systemMessage");
        systemMessage.setAlignment(Pos.CENTER);
        systemMessage.setWrapText(true);
        systemMessage.setMaxSize(250, 100);
        systemMessage.setMinSize(250, 100);


        MusicPlayerView musicPlayerView = MusicPlayerView.getInstance();
        ImageView musicImageView = musicPlayerView.getMusicImageView();

//        musicImageView.setTranslateX(background.getFitWidth() / 2 - 1465);
//        musicImageView.setTranslateY(MainState.WINDOW_HEIGHT / 2 - musicImageView.getFitHeight() - 55);


        Region emptyRegion = new Region();
        HBox.setHgrow(emptyRegion, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(timerLabel, emptyRegion,musicImageView, mapZoomButton, informationButton);

        playerBanners = new VBox();
        vBox.getChildren().addAll(hBox, systemMessage, playerBanners);
        vBox.setAlignment(Pos.TOP_CENTER);

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
    public void update(ArrayList<TrainCard> openCards) {
        if (openCards != null) {
            final String CLOSED_CARD_PATH = "images/traincards/traincard_back_small_rotated.png";
            ImageView closedTrainCard = new ImageView(new Image(CLOSED_CARD_PATH));
            ArrayList<ImageView> openTrainCards = new ArrayList<>();

            for (TrainCard openCard : openCards) {
                String cardColor = openCard.getColor();
                String path = "images/traincards/traincard_" + cardColor.toLowerCase() + "_small_rotated.png";
                openTrainCards.add(new ImageView(path));
            }

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
    public void update(String timerText) {
        timerLabel.setText(timerText);
    }

    @Override
    public void update(PlayerBanner playerBanner) {
        ArrayList<StackPane> stackPanes = new ArrayList<>();
        ArrayList<Player> players = playerBanner.getPlayers();

        for (int i = 0; i < players.size(); i++) {
            Text playerName = new Text("Player: " + players.get(i).getName());
            Text playerTrainCards = new Text("Traincards: " + players.get(i).getTrainCards().size());
            Text playerDestTickets = new Text("Tickets: " + players.get(i).getDestinationTickets().size());
            Text playerPoints = new Text("Points: " + players.get(i).getPoints());
            Text playerTrains = new Text("Trains: " + players.get(i).getTrains());

            playerName.getStyleClass().add("playerinfo");
            playerTrainCards.getStyleClass().add("playerinfo");
            playerDestTickets.getStyleClass().add("playerinfo");
            playerPoints.getStyleClass().add("playerinfo");
            playerTrains.getStyleClass().add("playerinfo");

            GridPane gridPane = new GridPane();
            gridPane.add(playerName, 0, 0, 2, 1);
            gridPane.add(playerTrainCards, 0, 1);
            gridPane.add(playerDestTickets, 1, rowIndex);
            gridPane.add(playerPoints, 0, 2);
            gridPane.add(playerTrains, 1, 2);
            gridPane.setHgap(10);
            gridPane.setTranslateX(40);
            gridPane.setTranslateY(17);

            ImageView playerBannerImageView = new ImageView("images/banners/player_banner_" + players.get(i).getPlayerColor().toLowerCase() + ".png");
            playerBannerImageView.setPreserveRatio(true);
            playerBannerImageView.setFitHeight(100);

            DropShadow borderGlow = new DropShadow();
            borderGlow.setOffsetX(0f);
            borderGlow.setOffsetY(0f);
            borderGlow.setColor(Color.YELLOW);
            borderGlow.setWidth(30);
            borderGlow.setHeight(30);

            if (players.get(i).isTurn()) {
                playerBannerImageView.setEffect(borderGlow);
            }

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(playerBannerImageView, gridPane);

            stackPanes.add(stackPane);
        }

        playerBanners.getChildren().removeAll(playerBanners.getChildren());
        playerBanners.getChildren().addAll(stackPanes);
    }

    @Override
    public void update(SystemMessage systemMessage) {
        this.systemMessage.setText(systemMessage.getMessage());
    }
}
