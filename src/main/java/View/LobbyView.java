package View;

import Controller.LobbyController;
import Controller.LoginController;
import Model.Player;
import Service.Observable;
import Service.Observer;
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
    private Text playerText;
    private Text message;
    private Text partyCode;

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

        VBox players = new VBox(10);
        players.setId("black_bg");
        players.setAlignment(Pos.TOP_CENTER);
        players.setPadding(new Insets(10));

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

        // Test eventlisteners
        playerText = new Text();
        playerText.setId("text");
        players.getChildren().add(playerText);

        grid.add(title, 0,0,1,1);
        grid.add(players, 0,1,1,1);
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
            Map<String, Object> players = (Map<String, Object>) data.get("players");
            Set<String> players_uuids = players.keySet();

            StringBuilder player_names = new StringBuilder();
            for (String id : players_uuids) {
                Map<String, Object> player = (Map<String, Object>) players.get(id);
                player_names.append(player.get("username"));
                player_names.append("\n");
            }

            this.playerText.setText(player_names.toString());
        }

        if (data.containsKey("message")) {
            this.message.setText((String) data.get("message"));
        }

        if (data.containsKey("partyCode")) {
            this.partyCode.setText((String) data.get("partyCode"));
        }
    }
}
