package App;

import Model.Player;
import Service.FirebaseService;
import javafx.stage.Stage;

public class MainState {
    public static final FirebaseService firebaseService = new FirebaseService();
    public static Stage primaryStage;

    public static String roomCode;
    public static String player_uuid;

    public static final int WINDOW_WIDTH = 1510;
    public static final int WINDOW_HEIGHT = 883;

    public static final String menuCSS = "css/mainMenu.css";
    public static final String gameCSS = "css/gameView.css";

    public static Player getLocalPlayer() {
        return firebaseService.getPlayerFromLobby(roomCode, player_uuid);
    }
}
