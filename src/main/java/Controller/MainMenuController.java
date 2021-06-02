package Controller;

import App.MainState;
import Model.MainMenu;
import View.GameView;
import View.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {
    private MainMenu mainMenu = new MainMenu();
    public void host() {
        mainMenu.viewLogin(true);
    }

    public void join() {
        mainMenu.viewLogin(false);
    }

    public void game() {
        Scene scene = new Scene(new GameView(MainState.primaryStage), MainState.SCREEN_WIDTH, MainState.SCREEN_HEIGHT);
        MainState.primaryStage.setScene(scene);
    }
}
