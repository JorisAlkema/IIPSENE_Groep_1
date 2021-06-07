package Controller;

import App.MainState;
import Model.Lobby;
import Model.Player;
import View.GameView;
import View.LobbyView;
import View.MainMenuView;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.util.ArrayList;

public class LobbyController {

    private Lobby lobby = new Lobby();

    public LobbyController(LobbyView lobbyView) {
        lobby.registerObserver(lobbyView);

        // Disconnect when player closes the program!
        MainState.primaryStage.setOnCloseRequest(e -> {
            disconnect();
        });

        Platform.runLater(this::attachListener);
    }

    public void leaveRoom() {
        disconnect();
        returnToMenu();
    }

    private void disconnect() {
        // remove listener && yourself from the room
        if (MainState.roomCode != null && MainState.player_uuid != null) {
            detachListener();
            MainState.firebaseService.removePlayer(MainState.roomCode, MainState.player_uuid);

            // If nobody is in the room, delete it.
            if (MainState.firebaseService.getPlayersFromLobby(MainState.roomCode).size() == 0) {
                MainState.firebaseService.getLobbyReference(MainState.roomCode).delete();
            }

            MainState.player_uuid = null;
            MainState.roomCode = null;
        }
    }

    private void attachListener() {
        lobby.setPlayerEventListener(MainState.firebaseService.getLobbyReference(MainState.roomCode).addSnapshotListener((document, e) -> {
            if (document != null && document.getData() != null) {
                lobby.notifyAllObservers(document, "updateDocument");

                if ((Boolean) document.getData().get("ongoing")) {

                    Platform.runLater(() -> {
                        detachListener();
                        Scene scene = new Scene(new GameView());
                        scene.getStylesheets().add(MainState.MenuCSS);
                        MainState.primaryStage.setScene(scene);
                    });
                }
            }
        }));
    }

    private void detachListener() {
        lobby.getPlayerEventListener().remove();
    }

    public void returnToMenu() {
        Scene scene = new Scene(new MainMenuView(), MainState.SCREEN_WIDTH, MainState.SCREEN_HEIGHT);
        scene.getStylesheets().add(MainState.MenuCSS);
        MainState.primaryStage.setScene(scene);
    }

    public void startRoom() {
        ArrayList<Player> allPlayers = MainState.firebaseService.getPlayersFromLobby(MainState.roomCode);


//        if (MainState.player.getHost() && allPlayers.size() >= 3) {
//            MainState.firebaseService.updateMessageInLobby(MainState.roomCode, "Game will start..\n");
//            MainState.firebaseService.updateOngoing(MainState.roomCode, true);
//        } else {
//            MainState.firebaseService.updateMessageInLobby(MainState.roomCode, "3 - 5 players are needed to start the game");
//        }

        MainState.firebaseService.updateOngoingOfLobby(MainState.roomCode, true);
    }
}
