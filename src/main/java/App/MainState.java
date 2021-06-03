package App;

import Model.Player;
import Service.FirebaseService;
import javafx.stage.Stage;

public class MainState {
    public static FirebaseService firebaseService = new FirebaseService();
    public static Stage primaryStage;

    public static String roomCode;
    public static Player player;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    public static final String MenuCSS = "css/styling.css";
}
