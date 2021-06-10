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

    private LobbyController controller = new LobbyController(this);;
    private Text message;
    private Text partyCode;
    private VBox players_wrapper;
    private VBox players;

    public LobbyView() {
        // Background Effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        // Background for textFields and return to menu button
        ImageView background = new ImageView("images/main_menu_background.jpg");
        background.setFitWidth(MainState.WINDOW_WIDTH);
        background.setFitHeight(MainState.WINDOW_HEIGHT);
        background.setEffect(colorAdjust);

        ImageView musicImageView = MusicPlayerView.getInstance().getMusicImageView();

        musicImageView.setTranslateX(background.getFitWidth() / 2 - 1465);
        musicImageView.setTranslateY(MainState.WINDOW_HEIGHT / 2 - musicImageView.getFitHeight() - 55);

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getColumnConstraints().add(new ColumnConstraints(1130)); // column 0 is 100 wide
        grid.getColumnConstraints().add(new ColumnConstraints(300)); // column 1 is 200 wide
        grid.getRowConstraints().add(new RowConstraints(97));
        grid.getRowConstraints().add(new RowConstraints(680));
        grid.setHgap(10);
        grid.setPadding(new Insets(40));

        ImageView title = new ImageView("images/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.5);
        title.setFitHeight(title.getImage().getHeight() * 0.5);

        players_wrapper = new VBox(10);
        players_wrapper.setId("black_bg");
        players_wrapper.setAlignment(Pos.TOP_CENTER);

        VBox info = new VBox(10);
//        info.setId("black_bg");
        info.setAlignment(Pos.TOP_CENTER);

        VBox map = new VBox(10);
        map.setId("black_bg");
        map.setAlignment(Pos.CENTER);
        map.setPadding(new Insets(10));
        Text titleMap = new Text("The map");
        titleMap.setId("text");
        titleMap.minHeight(100);
        ImageView imageMap = new ImageView("maps/map_big.jpg");
        imageMap.setPreserveRatio(true);
        imageMap.setFitWidth(300);
        map.getChildren().add(titleMap);
        map.getChildren().add(imageMap);

        VBox party = new VBox(10);
        party.setId("black_bg");
        party.setAlignment(Pos.CENTER);
        party.setPadding(new Insets(10));
        Text partyTitle = new Text("Partycode:");
        partyCode = new Text();
        partyTitle.setId("text");
        partyCode.setId("text");
        party.getChildren().add(partyTitle);
        party.getChildren().add(partyCode);

        VBox messageBox = new VBox(10);
        messageBox.setPrefHeight(300);
        messageBox.setId("black_bg");
        messageBox.setAlignment(Pos.TOP_CENTER);
        messageBox.setPadding(new Insets(10));
        message = new Text("Retrieving data...\n");
        message.setWrappingWidth(300);
        message.setId("text");
        messageBox.getChildren().add(message);

        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        Button leaveRoom = new Button("Leave Room");
        Button startRoom = new Button("Start Game");
        leaveRoom.setPrefWidth(300);
        startRoom.setPrefWidth(300);
        buttons.getChildren().add(leaveRoom);
        buttons.getChildren().add(startRoom);

        info.getChildren().add(map);
        info.getChildren().add(party);
        info.getChildren().add(messageBox);
        info.getChildren().add(buttons);


        players_wrapper.getChildren().add(createPlayerName("Players", "0/5"));
        players = new VBox(10);
        players.setPadding(new Insets(10));

        players_wrapper.getChildren().add(players);

        grid.add(title, 0,0,1,1);
        grid.add(players_wrapper, 0,1,1,1);
        grid.add(info, 1,1,1,1);


        // For now it returns to main menu
        leaveRoom.setOnMouseClicked(e -> controller.leaveRoom());
        startRoom.setOnMouseClicked(e -> controller.startRoom());
        getChildren().addAll(background, grid, musicImageView);
    }

    @Override
    public void update(DocumentSnapshot documentSnapshot) {
        Platform.runLater(() -> {
            GameState roomData = documentSnapshot.toObject(GameState.class);
            ArrayList<Player> allPlayers = roomData.getPlayers();

            players.getChildren().removeAll(players.getChildren());
            players_wrapper.getChildren().removeAll(players_wrapper.getChildren());
            players_wrapper.getChildren().add(createPlayerName("Players", String.format("%s/5", allPlayers.size())));
            players_wrapper.getChildren().add(players);

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

    public HBox createPlayerName(String name, String info) {
        HBox player_box = new HBox();
        player_box.setId("playerbox");
        Text player_name = new Text(name);
        player_name.setId("text");

        Text player_info = new Text(info);
        player_info.setId("text");

        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);

        player_box.getChildren().add(player_name);
        player_box.getChildren().add(r);
        player_box.getChildren().add(player_info);
        return player_box;
    }
}
