import View.MainMenuView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    // https://stackoverflow.com/a/52654791
    // "For Maven the solution is exactly the same: provide a new main class that doesn't extend from Application."
//    public static void main(String[] args) {
//        HelloFX.main(args);
//    }

    private final int SCREEN_WIDTH = 1280;
    private final int SCREEN_HEIGHT = 720;
    private final String CSS = "css/styling.css";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new MainMenuView(primaryStage), SCREEN_WIDTH, SCREEN_HEIGHT);
        scene.getStylesheets().add(CSS);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ticket to Ride");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
