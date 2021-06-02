package Controller;

import Model.MainMenu;
import View.GameView;
import View.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {
    private MainMenu mainMenu = new MainMenu();
    public void host(Stage primaryStage) {
        mainMenu.viewLogin(primaryStage, true);
    }

    public void join(Stage primaryStage) {
        mainMenu.viewLogin(primaryStage, false);
    }

    public void game(Stage primaryStage) {
        Scene scene = new Scene(new GameView(primaryStage), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        primaryStage.setScene(scene);
    }
}
