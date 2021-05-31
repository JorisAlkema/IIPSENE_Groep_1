package Model;

import View.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenu {
    public void viewLogin(Stage primaryStage, Boolean host) {
//        GameInfo gameInfo = new GameInfo(primaryStage);
//        gameInfo.stopTimer();
//        primaryStage.setOnCloseRequest(event -> gameInfo.stopTimer());

        Scene scene = new Scene(new LoginView(primaryStage, host), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
        String css = "css/styling.css";
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
    }
}
