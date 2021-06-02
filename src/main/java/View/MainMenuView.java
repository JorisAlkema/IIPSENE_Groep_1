package View;

import App.Main;
import App.MainState;
import Controller.MainMenuController;
import Service.Observable;
import Service.Observer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenuView extends StackPane implements Observer {
    private MainMenuController mainMenuController = new MainMenuController();
    public MainMenuView() {
        super();
        // App.Main layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        ImageView title = new ImageView("images/main_menu_logo.png");
        title.setFitWidth(title.getImage().getWidth() * 0.9);
        title.setFitHeight(title.getImage().getHeight() * 0.9);
        grid.add(title, 0,0,2,1);

        // Background
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);

        ImageView background = new ImageView("images/main_menu_background.jpg");
        background.setFitWidth(1280);
        background.setFitHeight(720);
        background.setEffect(colorAdjust);

        // Button layout
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.BOTTOM_LEFT);
        buttons.setPadding(new Insets(40));

        Button hostGame = new Button("Host a game");
        Button joinGame = new Button("Join a game");
        Button rules = new Button("Rules");
        Button quit = new Button("Quit");

        Button test = new Button("Testing");

        buttons.getChildren().add(test);
        buttons.getChildren().add(hostGame);
        buttons.getChildren().add(joinGame);
        buttons.getChildren().add(rules);
        buttons.getChildren().add(quit);

        getChildren().add(background);
        getChildren().add(grid);
        getChildren().add(buttons);

        //Events
        test.setOnMouseClicked(e -> mainMenuController.game());
        hostGame.setOnMouseClicked(e -> mainMenuController.host());
        joinGame.setOnMouseClicked(e -> mainMenuController.join());

        File rulesPDF = new File("src/main/resources/rules/ticket_to_ride_europe_rules.pdf");

        rules.setOnMouseClicked(e -> {
            try {
                openFile(rulesPDF);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        quit.setOnMouseClicked(e -> System.exit(0));
    }

    public static void openFile(File file) throws Exception {
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
