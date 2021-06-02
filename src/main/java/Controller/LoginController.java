package Controller;

import Model.Login;
import Service.FirebaseService;
import Service.Observer;
import View.MainMenuView;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginController {
    private Login login;

    public LoginController() {
        this.login = new Login();
    }

    // Return to Menu
    public void returnToMenu() {
        login.returnToMenu();
    }

    // Join game
    public void join(TextField inputUsername, TextField inputCode) {
        // Try to join lobby using these 2 arguments
        String username = inputUsername.getText();
        String code = inputCode.getText();
        login.join(username, code);
    }

    //Host game
    public void host(TextField inputUsername) {
        String username = inputUsername.getText();
        login.host(username);
    }

    public void addObserver(Observer observer) {
        login.registerObserver(observer);
    }
}
