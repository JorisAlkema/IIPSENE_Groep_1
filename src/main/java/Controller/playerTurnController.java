package Controller;


import App.MainState;
import Model.GameState;
import Model.Player;

import java.util.ArrayList;

public class playerTurnController {
//    private TurnTimer turnTimer = new TurnTimer();
    private Boolean isTurn = false;

    // Just in case if the player with turn leaves and the timer ends
    private String nextPlayerUUID;

    // Give turn to other player, only if you have host
    public void nextTurn(GameState gameState) {
        ArrayList<Player> players = gameState.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isTurn()) {
                players.get(i).setTurn(false);

                int nextPlayerIndex = (i + 1) % players.size();
                players.get(nextPlayerIndex).setTurn(true);

                calculateNextPlayer(gameState);
                break;
            }
        }
    }

    // Start turn system, only if you have host
    public void start(GameState gameState) {
        gameState.getPlayers().get(0).setTurn(true);
        calculateNextPlayer(gameState);
        System.out.println(nextPlayerUUID);
    }

    public void checkMyTurn(GameState gameState) throws Exception {
        System.out.println("CHECKED");
        if (gameState.getPlayer(MainState.player_uuid).isTurn()) {
            isTurn = true;
            System.out.println("It's your turn");
        } else {
            isTurn = false;
            if (getCurrent(gameState) != null) {
                System.out.println(getCurrent(gameState).getName() + " has the turn.");
            }
        }
        System.out.println(isTurn);

        if (!playerWithTurnLeft(gameState)) {
            if (nextPlayerUUID.equals(MainState.player_uuid)) {
                gameState.getPlayer(nextPlayerUUID).setTurn(true);
                throw new Exception("Player turn has been recalibrated");
            }
        }

        calculateNextPlayer(gameState);
    }

    //TODO: Calculate the next player when the player that has the turn leaves.
    private void calculateNextPlayer(GameState gameState) {
        ArrayList<Player> players = gameState.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isTurn()) {
                int nextPlayerIndex = (i + 1) % players.size();
                nextPlayerUUID = players.get(nextPlayerIndex).getUUID();
            }
        }
    }

    private Boolean playerWithTurnLeft(GameState gameState) {
        if (getCurrent(gameState) == null) {
            return false;
        }
        return true;
    }
    
    private Player getCurrent(GameState gameState) {
        for (Player player : gameState.getPlayers()) {
            if (player.isTurn()) {
                return player;
            }
        }
        return null;
    }

    public Boolean getTurn() {
        return isTurn;
    }
}
