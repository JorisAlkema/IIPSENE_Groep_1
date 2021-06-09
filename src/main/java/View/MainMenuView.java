package View;

import App.MainState;
import Controller.MainMenuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainMenuView extends StackPane {
    private MainMenuController mainMenuController = new MainMenuController();
    private MusicPlayerView musicPlayerView;

    public MainMenuView() {

        // App.Main layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));

        ImageView title = new ImageView("images/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.7);
        title.setFitHeight(title.getImage().getHeight() * 0.7);

        grid.add(title, 0,0,2,1);

        // Background
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        ImageView background = new ImageView("images/main_menu_background.jpg");
        background.setFitWidth(MainState.WINDOW_WIDTH);
        background.setFitHeight(MainState.WINDOW_HEIGHT);
        background.setEffect(colorAdjust);

        // Button layout
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.BOTTOM_LEFT);
        buttons.setPadding(new Insets(40));

        Button hostGame = new Button("Host a game");
        Button joinGame = new Button("Join a game");
        Button rules = new Button("Rules");
        Button quit = new Button("Quit");

        buttons.getChildren().add(hostGame);
        buttons.getChildren().add(joinGame);
        buttons.getChildren().add(rules);
        buttons.getChildren().add(quit);

        // Music button
        musicPlayerView = new MusicPlayerView();
        ImageView musicImageView = musicPlayerView.getMusicImageView();
        // There's probably a better way to align this, but at least the menu buttons are clickable now
        musicImageView.setTranslateX(background.getFitWidth() / 2 - musicImageView.getFitWidth() - 45);
        musicImageView.setTranslateY(background.getFitHeight() / 2 - musicImageView.getFitHeight() - 40);

        getChildren().add(background);
        getChildren().add(grid);
        getChildren().add(buttons);
        getChildren().add(musicImageView);

        //Events
        hostGame.setOnMouseClicked(e -> mainMenuController.host());
        joinGame.setOnMouseClicked(e -> mainMenuController.join());
        rules.setOnMouseClicked(e -> mainMenuController.openRules());
        quit.setOnMouseClicked(e -> System.exit(0));
    }
}
