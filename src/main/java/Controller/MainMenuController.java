package Controller;

import Model.MainMenu;
import javafx.stage.Stage;

public class MainMenuController {
    private MainMenu mainMenu = new MainMenu();
    public void host(Stage primaryStage) {
        mainMenu.viewLogin(primaryStage, true);
    }

    public void join(Stage primaryStage) {
        mainMenu.viewLogin(primaryStage, false);
    }
}
