package Controller;

import Model.Login;
import Service.FirebaseService;
import View.MainMenuView;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginController {
    private Login login = new Login();

    // Return to Menu
    public void returnToMenu(Stage primaryStage) {
        login.returnToMenu(primaryStage);
    }

    // Join game
    public void join(TextField inputUsername, TextField inputCode) {
        // Try to join lobby using these 2 arguments
        login.join(inputUsername, inputCode);

    }

    //Host game
    public void host(TextField inputUsername) {
        System.out.println(String.format("%s wants to host a game", inputUsername.getText()));
    }
}
