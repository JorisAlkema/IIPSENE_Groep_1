package App;

import Model.MusicPlayer;
import Model.Player;
import Service.FirebaseService;
import javafx.stage.Stage;

public class MainState {
    public static FirebaseService firebaseService = new FirebaseService();
    public static Stage primaryStage;
    public static Player getLocalPlayer() {
        return firebaseService.getPlayerFromLobby(roomCode, player_uuid);
    }
    public static String roomCode;
//    public static Player player;
    public static String player_uuid;

    public static MusicPlayer musicPlayer;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    public static final String MenuCSS = "css/mainMenuStyle.css";
    public static final String mainCSS = "css/mainStyle.css";

    public static Player getLocalPlayer() {
        return firebaseService.getPlayerFromLobby(roomCode, player_uuid);
    }
}
