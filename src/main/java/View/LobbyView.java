package View;

import Controller.LobbyController;
import Controller.LoginController;
import Model.Player;
import Service.Observable;
import Service.Observer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class LobbyView extends StackPane implements Observer {

    private LobbyController controller;
    private Text message;
    private Text partyCode;
    private VBox players_wrapper;
    private VBox players;

    public LobbyView(Stage primaryStage, String player_uuid, String roomCode) {
        controller = new LobbyController(primaryStage, player_uuid, roomCode);
        controller.addObserver(this);

        // Background Effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        // Background for textFields and return to menu button
        ImageView background = new ImageView("images/main_menu_background.jpg");
        background.setFitWidth(1280);
        background.setFitHeight(720);
        background.setEffect(colorAdjust);

        // Layout
        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(890)); // column 0 is 100 wide
        grid.getColumnConstraints().add(new ColumnConstraints(300)); // column 1 is 200 wide
        grid.getRowConstraints().add(new RowConstraints(100));
        grid.getRowConstraints().add(new RowConstraints(580));
        grid.setHgap(10);
        grid.setPadding(new Insets(40));

        ImageView title = new ImageView("images/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.4);
        title.setFitHeight(title.getImage().getHeight() * 0.4);

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
        messageBox.setId("black_bg");
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(10));
        message = new Text();
        message.setWrappingWidth(300);
        message.setId("text");
        messageBox.getChildren().add(message);

        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        Button leaveRoom = new Button("Leave Room");
        Button startGame = new Button("Start Game");
        leaveRoom.setPrefWidth(300);
        startGame.setPrefWidth(300);
        buttons.getChildren().add(leaveRoom);
        buttons.getChildren().add(startGame);

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

        getChildren().add(background);
        getChildren().add(grid);
    }
    @Override
    public void update(Observable observable, Object o) {
        Map<String, Object> data = (Map<String, Object>) o;

        if (data.containsKey("players")) {
            Map<String, Object> all_players = (Map<String, Object>) data.get("players");
            Set<String> players_uuids = all_players.keySet();

            Platform.runLater(() -> {
                players_wrapper.getChildren().removeAll(players_wrapper.getChildren());
                players_wrapper.getChildren().add(createPlayerName("Players", String.format("%s/5", all_players.size())));
                players_wrapper.getChildren().add(players);
                players.getChildren().removeAll(players.getChildren());
                for (String id : players_uuids) {
                    Map<String, Object> player = (Map<String, Object>) all_players.get(id);
                    String info = (Boolean) player.get("host") ? "Host" : "Player";
                    if ((Boolean) player.get("host")) {
                        players.getChildren().add(0, createPlayerName((String) player.get("username"), info));
                    } else {
                        players.getChildren().add(createPlayerName((String) player.get("username"), info));
                    }
                }
            });
        }

        if (data.containsKey("message")) {
            this.message.setText((String) data.get("message"));
        }

        if (data.containsKey("partyCode")) {
            this.partyCode.setText((String) data.get("partyCode"));
        }
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
