package Controller;

import View.MainMenuView;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    // Return to Menu
    public void returnToMenu(Stage primaryStage) {
        Scene scene = new Scene(new MainMenuView(), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        String css = "css/styling.css";
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
    }

    // Join game
    public void join(TextField inputUsername, TextField inputCode) {
        String username = inputUsername.getText();
        String code = inputCode.getText();
        System.out.println(String.format("User pressed join with the following inputs:\nUsername: %s\nCode: %s", username, code));
    }
}
