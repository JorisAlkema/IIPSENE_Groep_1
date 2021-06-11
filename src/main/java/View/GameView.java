package View;

import App.MainState;
import Controller.GameController;
import Model.Player;
import Model.PlayerBanner;
import Model.PlayerTurn;
import Model.TrainCard;
import Observers.BannerObserver;
import Observers.CardsObserver;
import Observers.PlayerTurnObverser;
import Observers.TurnTimerObserver;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GameView extends StackPane implements TurnTimerObserver, CardsObserver, BannerObserver {
    private Label timerLabel;
    private final BorderPane borderPane;
    private VBox cardsBox;
    private VBox playerBanners;
    private MapView mapView;
    private final GameController gameController;

    public GameView() {
        // Init
        gameController = new GameController();
        gameController.registerTurnTimerObserver(this);
        gameController.registerCardsObserver(this);
        gameController.registerBannerObserver(this);

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

        MusicPlayerView musicPlayerView = MusicPlayerView.getInstance();
        ImageView musicImageView = musicPlayerView.getMusicImageView();

        musicImageView.setTranslateX(background.getFitWidth() / 2 - 1465);
        musicImageView.setTranslateY(MainState.WINDOW_HEIGHT / 2 - musicImageView.getFitHeight() - 55);

        this.getChildren().addAll(background, borderPane, musicImageView);
    }

    private void initLeftPane() {
        Image zoomInImage = new Image("images/icons/button_zoom_in.png");
        Image zoomOutImage = new Image("images/icons/button_zoom_out.png");
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

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));

        playerBanners = new VBox();

        vBox.getChildren().addAll(timerLabel, mapZoomButton, playerBanners);
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
                String path = "images/traincards/traincard_" + cardColor + "_small_rotated.png";
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
    public void update(String timerText) {
        timerLabel.setText(timerText);
    }

    @Override
    public void update(PlayerBanner playerBanner) {
        ArrayList<StackPane> stackPanes = new ArrayList<>();
        ArrayList<ImageView> banners = new ArrayList<>();
        ArrayList<Player> players = playerBanner.getPlayers();

        banners.add(new ImageView("images/banners/player_banner_green.png"));
        banners.add(new ImageView("images/banners/player_banner_blue.png"));
        banners.add(new ImageView("images/banners/player_banner_purple.png"));
        banners.add(new ImageView("images/banners/player_banner_red.png"));
        banners.add(new ImageView("images/banners/player_banner_yellow.png"));

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
            gridPane.add(playerDestTickets, 1, 1);
            gridPane.add(playerPoints, 0, 2);
            gridPane.add(playerTrains, 1, 2);
            gridPane.setHgap(10);
            gridPane.setTranslateX(40);
            gridPane.setTranslateY(17);

            ImageView playerBannerImageView = banners.get(i);
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
}
