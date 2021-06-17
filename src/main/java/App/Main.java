package App;

import View.MainMenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        MainState.primaryStage = primaryStage;
        MainMenuView mainMenuView = new MainMenuView();
        Scene scene = new Scene(mainMenuView);
        scene.getStylesheets().add(MainState.menuCSS);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Ticket to Ride");
        primaryStage.getIcons().add(new Image("images/icons/ttr_icon_main.png"));
        primaryStage.setResizable(false);
        primaryStage.setHeight(MainState.WINDOW_HEIGHT);
        primaryStage.setWidth(MainState.WINDOW_WIDTH);
        primaryStage.show();
    }
}
