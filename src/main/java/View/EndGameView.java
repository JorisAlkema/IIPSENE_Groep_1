package View;

import App.MainState;
import Controller.EndGameController;
import Model.GameState;
import Model.MusicPlayer;
import Model.Player;
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

    public EndGameView(GameState gameState) {
        endGameController = new EndGameController(gameState);

        ImageView background = createBackGround();
        GridPane gridPane = createGridPane();
        ImageView title = createTitle();
        VBox playersWrapper = createPlayersWrapper();
        VBox infoBox = createInfoBox();
        StackPane stackPane = createStackPane();

        gridPane.add(title, 0,0);
        gridPane.add(playersWrapper, 0,1);
        gridPane.add(infoBox, 1,1);
        gridPane.add(stackPane, 0, 1);

        this.getChildren().add(background);
        this.getChildren().add(gridPane);
        MusicPlayer.getInstance().playEndMusic();
    }

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

        gridPane.getColumnConstraints().add(new ColumnConstraints(1130));
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

    private VBox createPlayersWrapper() {
        ImageView podium = new ImageView("images/endgame/podium.png");

        VBox playersWrapper = new VBox(10);

        playersWrapper.setId("black_bg");
        playersWrapper.getChildren().add(podium);
        playersWrapper.setAlignment(Pos.BOTTOM_CENTER);

        return playersWrapper;
    }

    // StackPane with player positions
    private StackPane createStackPane() {
        StackPane stackPane = new StackPane();

        int i = 0;
        for (Player player : endGameController.getSortedPlayersByPoints()) {
            Label playerNameLabel = new Label(player.getName() + "\nPoints: " + player.getPoints());
            playerNameLabel.setId("podiumText");

            switch (i) {
                case 0:
                    playerNameLabel.setTranslateX(-5);
                    break;
                case 1:
                    playerNameLabel.setTranslateX(-270);
                    playerNameLabel.setTranslateY(40);
                    break;
                case 2:
                    playerNameLabel.setTranslateX(270);
                    playerNameLabel.setTranslateY(70);
                    break;
            }

            playerNameLabel.setAlignment(Pos.CENTER);
            stackPane.getChildren().add(playerNameLabel);
            i++;
        }
        return stackPane;
    }

    private VBox createInfoBox() {
        VBox info = new VBox(10);
        info.setAlignment(Pos.TOP_CENTER);

        VBox map = new VBox(10);
        map.setId("black_bg");
        map.setAlignment(Pos.CENTER);
        map.setPadding(new Insets(10));
        Text titleMap = new Text("The map");
        titleMap.setId("text");
        titleMap.minHeight(100);
        ImageView imageMap = new ImageView("images/maps/map_small.jpg");
        imageMap.setPreserveRatio(true);
        imageMap.setFitWidth(300);
        map.getChildren().add(titleMap);
        map.getChildren().add(imageMap);

        VBox buttons = new VBox(20);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        Button mainMenu = new Button("Main Menu");
        mainMenu.setPrefWidth(300);
        mainMenu.setOnMouseClicked(e -> endGameController.toMenu());
        buttons.getChildren().add(mainMenu);

        info.getChildren().add(map);
        info.getChildren().add(buttons);
        return info;
    }
}
