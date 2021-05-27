package View;

import Controller.LobbyController;
import Controller.LoginController;
import Service.Observable;
import Service.Observer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LobbyView extends StackPane implements Observer {

    private LobbyController controller;
    private Text players;

    public LobbyView(Stage primaryStage, String player_uuid, String roomCode) {
        controller = new LobbyController(primaryStage, player_uuid, roomCode);
        controller.addObserver(this);
        // Logo
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        ImageView title = new ImageView("images/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.5);
        title.setFitHeight(title.getImage().getHeight() * 0.5);
        grid.add(title, 0,0,2,1);

        // Background Effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        // Background for textFields and return to menu button
        ImageView background = new ImageView("images/main_menu_background.jpg");
        background.setFitWidth(1280);
        background.setFitHeight(720);
        background.setEffect(colorAdjust);

        // Layout
        VBox app = new VBox(10);
        app.setAlignment(Pos.BOTTOM_LEFT);
        app.setPadding(new Insets(40));
        Button leaveRoom = new Button("Leave Room");

        // Test eventlisteners
        players = new Text();
        players.setId("text");

        app.getChildren().add(players);
        app.getChildren().add(leaveRoom);

        // For now it returns to main menu
        leaveRoom.setOnMouseClicked(e -> controller.leaveRoom());

        getChildren().add(background);
        getChildren().add(grid);
        getChildren().add(app);

    }
    @Override
    public void update(Observable observable, Object o) {
        this.players.setText((String) o);
    }
}
