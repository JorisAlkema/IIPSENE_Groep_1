package Controller;

import Model.Lobby;
import Service.Observer;
import javafx.stage.Stage;

public class LobbyController {
    private Lobby lobby;
    public LobbyController(String player_uuid, String roomCode) {
        lobby = new Lobby(player_uuid, roomCode);
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
