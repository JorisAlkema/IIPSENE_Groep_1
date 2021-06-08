package Controller;

import App.Main;
import App.MainState;
import Model.GameState;
import Model.Player;
import Model.TrainCard;
import com.google.cloud.firestore.ListenerRegistration;
import javafx.application.Platform;

import java.util.ArrayList;

public class playerTurnController {
    private GameController gameController;
    private ListenerRegistration listenerRegistration;

    private Boolean isTurn = false;
    private String currentTurnUUID;

    public playerTurnController(GameController gameController) {
        this.gameController = gameController;
        checkTurnListener();

        // if host
        if (MainState.firebaseService.getPlayerFromLobby(MainState.roomCode, MainState.player_uuid).getHost()) {
            giveTurnToFirstPlayer();
        }
    }

    public void giveTurnToFirstPlayer() {
        GameState gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
        gameState.getPlayers().get(0).setTurn(true);
        MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
    }

    /*
    * EndTurn and set Next Player turn
    * */
    public void endTurn() {
        GameState gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
        ArrayList<Player> players = gameState.getPlayers();
        Player player = gameState.getPlayer(MainState.player_uuid);

        // Add turn to next player
        int nextIndex = (players.indexOf(player) + 1) % players.size();
        player.setTurn(false);
        players.get(nextIndex).setTurn(true);

        MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
    }

    /*
    *
    * */
    public void checkTurnListener() {
        listenerRegistration = MainState.firebaseService.getLobbyReference(MainState.roomCode).addSnapshotListener((document, e) -> {
            GameState gameState = document.toObject(GameState.class);
            ArrayList<Player> players = gameState.getPlayers();
            for (Player player : players) {
                if (player.isTurn()) {
                    if (!player.getUUID().equals(currentTurnUUID)) {
                        currentTurnUUID = player.getUUID();
                        if (player.getUUID().equals(MainState.player_uuid)) {
                            this.isTurn = true;
                            System.out.println("YOUR TURN");
                        } else {
                            this.isTurn = false;
                            System.out.println("TURN FOR " + player.getName());
                        }
                        Platform.runLater(() -> {
                            gameController.stopTimer();
                            gameController.countdownTimer();
                        });
                    }
                    break;
                }
            }
        });
    }
}
