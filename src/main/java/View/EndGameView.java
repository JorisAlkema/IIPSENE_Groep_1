package View;

import App.MainState;
import Controller.EndGameController;
import Model.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class EndGameView extends StackPane {
    private final EndGameController endGameController;
    private VBox players_wrapper;
    private VBox players;

    public EndGameView(GameState gameState) {
        endGameController = new EndGameController(gameState);

        // Background Effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        // Background for textFields and return to menu button
        ImageView background = new ImageView("images/backgrounds/main_menu_background.jpg");
        background.setFitWidth(MainState.WINDOW_WIDTH);
        background.setFitHeight(MainState.WINDOW_HEIGHT);
        background.setEffect(colorAdjust);

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getColumnConstraints().add(new ColumnConstraints(1130)); // column 0 is 100 wide
        grid.getColumnConstraints().add(new ColumnConstraints(300)); // column 1 is 200 wide
        grid.getRowConstraints().add(new RowConstraints(97));
        grid.getRowConstraints().add(new RowConstraints(680));
        grid.setHgap(10);
        grid.setPadding(new Insets(40));

        ImageView title = new ImageView("images/logos/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.5);
        title.setFitHeight(title.getImage().getHeight() * 0.5);

        ImageView podium = new ImageView("images/endgame/podium.png");
        players_wrapper = new VBox(10);
        players_wrapper.setId("black_bg");
        players_wrapper.getChildren().add(podium);
        players_wrapper.setAlignment(Pos.BOTTOM_CENTER);

        StackPane stackPane = new StackPane();
        int i = 0;
        for (String playerName : endGameController.topThreePlayers()) {
            Label playerNameLabel = new Label(playerName);
            playerNameLabel.setId("podiumText");
            switch (i) {
                case 2:
                    stackPane.getChildren().add(playerNameLabel);
                    playerNameLabel.setAlignment(Pos.CENTER);
                    playerNameLabel.setTranslateX(-5);
                    i++;
                    break;
                case 1:
                    stackPane.getChildren().add(playerNameLabel);
                    playerNameLabel.setAlignment(Pos.CENTER);
                    playerNameLabel.setTranslateX(-270);
                    playerNameLabel.setTranslateY(40);
                    i++;
                    break;
                case 0:
                    stackPane.getChildren().add(playerNameLabel);
                    playerNameLabel.setAlignment(Pos.CENTER);
                    playerNameLabel.setTranslateX(270);
                    playerNameLabel.setTranslateY(70);
                    i++;
                    break;
            }
        }

        // Right side info box
        VBox info = new VBox(10);
        //info.setId("black_bg");
        info.setAlignment(Pos.TOP_CENTER);

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

        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        Button mainMenu = new Button("Main Menu");
        mainMenu.setPrefWidth(300);
        buttons.getChildren().add(mainMenu);

        info.getChildren().add(map);
        info.getChildren().add(buttons);

        players = new VBox(10);
        players.setPadding(new Insets(10));

        grid.add(title, 0,0,1,1);
        grid.add(players_wrapper, 0,1,1,1);
        grid.add(info, 1,1,1,1);
        grid.add(stackPane, 0, 1, 1, 1);

        mainMenu.setOnMouseClicked(e -> endGameController.toMenu());
        getChildren().add(background);
        getChildren().add(grid);
    }
}
