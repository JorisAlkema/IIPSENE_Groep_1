package Controller;

import App.MainState;
import Model.Lobby;
import Model.Player;
import View.GameView;
import View.LobbyView;
import View.MainMenuView;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

public class LobbyController {

    private final Lobby lobby = new Lobby();

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
                lobby.notifyObservers(document);

                if ((Boolean) document.getData().get("ongoing")) {

                    Platform.runLater(() -> {
                        detachListener();
                        Scene scene = new Scene(new GameView());
                        scene.getStylesheets().add(MainState.gameCSS);
                        MainState.primaryStage.setScene(scene);
                        //MainState.primaryStage.setX(MainState.WINDOW_X_POSITION);
                        //MainState.primaryStage.setY(MainState.WINDOW_Y_POSITION);
                    });
                }
            }
        }));
    }

    private void detachListener() {
        lobby.getPlayerEventListener().remove();
    }

    public void returnToMenu() {
        Scene scene = new Scene(new MainMenuView(), MainState.WINDOW_WIDTH, MainState.WINDOW_HEIGHT);
        scene.getStylesheets().add(MainState.menuCSS);
        MainState.primaryStage.setScene(scene);
    }

    public void startRoom() {
        ArrayList<Player> allPlayers = MainState.firebaseService.getPlayersFromLobby(MainState.roomCode);

        if (MainState.getLocalPlayer().getHost()) {
            if (allPlayers.size() >= 3) {
                MainState.firebaseService.updateMessageOfLobby(MainState.roomCode, "Game will start..\n");
                MainState.firebaseService.updateOngoingOfLobby(MainState.roomCode, true);
            } else {
                MainState.firebaseService.updateMessageOfLobby(MainState.roomCode, "3 or more players are needed to start the game");
            }
        } else {
            MainState.firebaseService.updateMessageOfLobby(MainState.roomCode, MainState.getLocalPlayer().getName() + " wants to start the game");
        }

        // Remove from production
//        MainState.firebaseService.updateOngoingOfLobby(MainState.roomCode, true);
    }

    public void copyRoomCode() {
        StringSelection stringSelection = new StringSelection(MainState.roomCode);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
