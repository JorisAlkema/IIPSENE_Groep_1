package Controller;

import App.MainState;
import View.LoginView;
import javafx.scene.Scene;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MainMenuController {

    public static void openRules() throws IOException {
        String filepath = "rules/ticket_to_ride_europe_rules.pdf";
        InputStream inputStream = MainMenuController.class.getClassLoader().getResourceAsStream(filepath);

        Path tempOutput = Files.createTempFile("TempManual", ".pdf");
        tempOutput.toFile().deleteOnExit();

        Files.copy(inputStream, tempOutput, StandardCopyOption.REPLACE_EXISTING);

        File file = new File(tempOutput.toFile().getPath());
        if (Desktop.isDesktopSupported()) {
            File finalFile = file;
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(finalFile);
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
