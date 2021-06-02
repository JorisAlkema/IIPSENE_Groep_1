package Controller;

import Model.Lobby;
import Service.Observer;
import javafx.stage.Stage;

public class LobbyController {
    private Lobby lobby;
    public LobbyController(Stage primaryStage, String player_uuid, String roomCode) {
        lobby = new Lobby(primaryStage, player_uuid, roomCode);
    }

    public void leaveRoom() {
        lobby.disconnect();
        lobby.returnToMenu();
    }

    public void startRoom() {
        lobby.startRoom();
    }

    public void addObserver(Observer observer) {
        lobby.registerObserver(observer);
    }
}
