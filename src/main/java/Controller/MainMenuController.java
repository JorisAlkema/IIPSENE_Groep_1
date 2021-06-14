package Controller;

import App.MainState;
import View.LoginView;
import javafx.scene.Scene;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainMenuController {

    public static void openRules() {
        File rulesPDF = new File("src/main/resources/rules/ticket_to_ride_europe_rules.pdf");

        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(rulesPDF);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void host() {
        switchScene(true);
    }

    public void join() {
        switchScene(false);
    }

    private void switchScene(Boolean isHost) {
        Scene scene = new Scene(new LoginView(isHost), MainState.WINDOW_WIDTH, MainState.WINDOW_HEIGHT);
        scene.getStylesheets().add(MainState.menuCSS);
        MainState.primaryStage.setScene(scene);
    }
}
