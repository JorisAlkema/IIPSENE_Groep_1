package Controller;


import App.MainState;
import Model.GameState;
import Model.Player;
import Model.PlayerTurn;
import Observers.PlayerTurnObverser;

import java.util.ArrayList;

public class PlayerTurnController {
    private final PlayerTurn playerTurn = new PlayerTurn();
    // Just in case if the player with turn leaves and the timer ends

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
        System.out.println(playerTurn.getNextPlayerUUID());
    }

    public void checkMyTurn(GameState gameState) throws Exception {
        System.out.println("CHECKED");

        if (!playerWithTurnLeft(gameState)) {
            if (playerTurn.getNextPlayerUUID().equals(MainState.player_uuid)) {
                gameState.getPlayer(playerTurn.getNextPlayerUUID()).setTurn(true);
                throw new Exception("Player turn has been recalibrated");
            }
        }

        if (gameState.getPlayer(MainState.player_uuid).isTurn()) {
            playerTurn.setTurn(true);
            System.out.println("It's your turn");
        } else {
            playerTurn.setTurn(false);
            System.out.println(getCurrent(gameState).getName() + " has the turn.");
        }

        playerTurn.setCurrentPlayerUsername(getCurrent(gameState).getName());
        calculateNextPlayer(gameState);
    }

    //TODO: Calculate the next player when the player that has the turn leaves.
    private void calculateNextPlayer(GameState gameState) {
        ArrayList<Player> players = gameState.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isTurn()) {
                int nextPlayerIndex = (i + 1) % players.size();
                playerTurn.setNextPlayerUUID(players.get(nextPlayerIndex).getUUID());
            }
        }
    }

    private Boolean playerWithTurnLeft(GameState gameState) {
        return getCurrent(gameState) != null;
    }

    public Player getCurrent(GameState gameState) {
        for (Player player : gameState.getPlayers()) {
            if (player.isTurn()) {
                return player;
            }
        }
        return null;
    }

    public Boolean getTurn() {
        return playerTurn.getTurn();
    }

    public void registerObserver(PlayerTurnObverser playerTurnObverser) {
        playerTurn.registerObserver(playerTurnObverser);
    }
}
