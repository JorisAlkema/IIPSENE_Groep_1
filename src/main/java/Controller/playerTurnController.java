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

    /**
     * When you start the game run this function only once
     */
    public void giveTurnToFirstPlayer() {
        GameState gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
        gameState.getPlayers().get(0).setTurn(true);
        MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
    }

    /**
     * End the turn of yourself when some conditions are met, or the timer is finished.
     * If you call this function and it's not your turn, nothing happens.
     * Otherwise it sets your players object's turn to false and sets the next player's isTurn to true.
    */
    public void endTurn() {
        GameState gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
        ArrayList<Player> players = gameState.getPlayers();
        Player player = gameState.getPlayer(MainState.player_uuid);

        int nextIndex = (players.indexOf(player) + 1) % players.size();
        player.setTurn(false);
        players.get(nextIndex).setTurn(true);
        
        MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
    }

    /**
     * Check the turn on new room update event.
     * When the currentTurnUUID is the same, it means the turn is still on the same player. So therefore ignore it
     * If the player object turn is true and the object belongs to your id, that means it's your turn. isTurn = true.
     */
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
                        } else {
                            this.isTurn = false;
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


    public Boolean isTurn() {
        return isTurn;
    }
}
