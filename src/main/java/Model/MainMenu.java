package Model;

import App.MainState;
import View.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenu {
    public void viewLogin(Boolean host) {
//        GameInfo gameInfo = new GameInfo(primaryStage);
//        gameInfo.stopTimer();
//        primaryStage.setOnCloseRequest(event -> gameInfo.stopTimer());

        Scene scene = new Scene(new LoginView(host), MainState.primaryStage.getScene().getWidth(), MainState.primaryStage.getScene().getHeight());
        String css = "css/styling.css";
        scene.getStylesheets().add(css);
        MainState.primaryStage.setScene(scene);
    }
}
