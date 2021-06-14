package View;

import App.MainState;
import Controller.LobbyController;
import Model.GameState;
import Model.Player;
import Observers.LobbyObserver;
import com.google.cloud.firestore.DocumentSnapshot;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class LobbyView extends StackPane implements LobbyObserver {

    private final LobbyController controller;
    private Text message;
    private Text partyCode;
    private final VBox playersWrapper;
    private final VBox players;

    public LobbyView() {
        this.controller = new LobbyController(this);

        ImageView background = createBackGround();
        GridPane gridPane = createGridPane();
        ImageView title = createTitle();

        playersWrapper = new VBox(10);
        playersWrapper.setId("black_bg");
        playersWrapper.setAlignment(Pos.TOP_CENTER);

        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_CENTER);

        VBox map = createMapInfoBox();
        VBox party = createPartyInfoBox();
        VBox messageBox = createMessageBox();
        VBox buttons = createButtons();

        infoBox.getChildren().add(map);
        infoBox.getChildren().add(party);
        infoBox.getChildren().add(messageBox);
        infoBox.getChildren().add(buttons);

        playersWrapper.getChildren().add(createPlayerName("Players", "0/5"));
        players = new VBox(10);
        players.setPadding(new Insets(10));
        playersWrapper.getChildren().add(players);

        gridPane.add(title, 0, 0);
        gridPane.add(playersWrapper, 0, 1);
        gridPane.add(infoBox, 1, 1);

        this.getChildren().add(background);
        this.getChildren().add(gridPane);
    }

    @Override
    public void update(DocumentSnapshot documentSnapshot) {
        Platform.runLater(() -> {
            GameState roomData = documentSnapshot.toObject(GameState.class);
            ArrayList<Player> allPlayers = roomData.getPlayers();

            players.getChildren().removeAll(players.getChildren());
            playersWrapper.getChildren().removeAll(playersWrapper.getChildren());
            playersWrapper.getChildren().add(createPlayerName("Players", String.format("%s/5", allPlayers.size())));
            playersWrapper.getChildren().add(players);

            for (Player x : allPlayers) {
                String username = x.getName();
                String info = x.getHost() ? "Host" : "Player";
                HBox player_card = createPlayerName(username, info);
                if (x.getHost()) {
                    players.getChildren().add(0, player_card);
                } else {
                    players.getChildren().add(player_card);
                }
            }

            this.message.setText(roomData.getMessage());
            this.partyCode.setText(MainState.roomCode);
        });
    }

    private HBox createPlayerName(String name, String info) {
        HBox player_box = new HBox();
        player_box.setId("playerbox");
        Text player_name = new Text(name);
        player_name.setId("text");
        Text player_info = new Text(info);
        player_info.setId("text");

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        player_box.getChildren().add(player_name);
        player_box.getChildren().add(region);
        player_box.getChildren().add(player_info);
        return player_box;
    }

    // Background image for the whole scene
    private ImageView createBackGround() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        ImageView background = new ImageView("images/backgrounds/main_menu_background.jpg");
        background.setFitWidth(MainState.WINDOW_WIDTH);
        background.setFitHeight(MainState.WINDOW_HEIGHT);
        background.setEffect(colorAdjust);
        return background;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.getColumnConstraints().add(new ColumnConstraints(1080));
        gridPane.getColumnConstraints().add(new ColumnConstraints(300));
        gridPane.getRowConstraints().add(new RowConstraints(97));
        gridPane.getRowConstraints().add(new RowConstraints(680));
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(40));
        return gridPane;
    }

    private ImageView createTitle() {
        ImageView title = new ImageView("images/logos/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.5);
        title.setFitHeight(title.getImage().getHeight() * 0.5);
        return title;
    }

    // Right side map + info
    private VBox createMapInfoBox() {
        VBox map = new VBox(10);
        map.setId("black_bg");
        map.setAlignment(Pos.CENTER);
        map.setPadding(new Insets(10));
        Text titleMap = new Text("The map");
        titleMap.setId("text");
        titleMap.minHeight(100);
        ImageView imageMap = new ImageView("images/maps/map_big.jpg");
        imageMap.setPreserveRatio(true);
        imageMap.setFitWidth(300);
        map.getChildren().add(titleMap);
        map.getChildren().add(imageMap);
        return map;
    }

    // Right side party info
    private VBox createPartyInfoBox() {
        VBox party = new VBox(10);
        party.setId("black_bg");
        party.setAlignment(Pos.CENTER);
        party.setPadding(new Insets(10));
        Text partyTitle = new Text("Partycode:");
        this.partyCode = new Text();
        partyTitle.setId("text");
        this.partyCode.setId("text");
        party.getChildren().add(partyTitle);
        party.getChildren().add(this.partyCode);
        return party;
    }

    private VBox createMessageBox() {
        VBox messageBox = new VBox(10);
        messageBox.setPrefHeight(300);
        messageBox.setId("black_bg");
        messageBox.setAlignment(Pos.TOP_CENTER);
        messageBox.setPadding(new Insets(10));
        this.message = new Text("Retrieving data...\n");
        this.message.setWrappingWidth(300);
        this.message.setId("text");
        messageBox.getChildren().add(this.message);
        return messageBox;
    }

    // Right side buttons
    private VBox createButtons() {
        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        Button leaveRoomButton = new Button("Leave Room");
        Button startRoomButton = new Button("Start Game");
        leaveRoomButton.setPrefWidth(300);
        startRoomButton.setPrefWidth(300);
        leaveRoomButton.setOnMouseClicked(e -> controller.leaveRoom());
        startRoomButton.setOnMouseClicked(e -> controller.startRoom());
        buttons.getChildren().add(leaveRoomButton);
        buttons.getChildren().add(startRoomButton);
        return buttons;
    }
}
