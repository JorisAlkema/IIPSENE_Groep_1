package App;

import App.MainState;
import View.MainMenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // https://stackoverflow.com/a/52654791
    // "For Maven the solution is exactly the same: provide a new main class that doesn't extend from Application."
//    public static void main(String[] args) {
//        HelloFX.main(args);
//    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainState.primaryStage = primaryStage;
        MainMenuView mainMenuView = new MainMenuView();
        Scene scene = new Scene(mainMenuView, MainState.SCREEN_WIDTH, MainState.SCREEN_HEIGHT);
        scene.getStylesheets().add(MainState.MenuCSS);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ticket to Ride");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
