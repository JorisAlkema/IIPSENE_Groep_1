package Model;

import Observers.PlayerTurnObservable;
import Observers.PlayerTurnObverser;

import java.util.ArrayList;

public class PlayerTurn implements PlayerTurnObservable {
    private ArrayList<PlayerTurnObverser> playerTurnObversers = new ArrayList<>();

    private String currentPlayerUsername;

    private Boolean isTurn = false;
    private String nextPlayerUUID;

    public String getCurrentPlayerUsername() {
        return currentPlayerUsername;
    }

    public void setCurrentPlayerUsername(String currentPlayerUsername) {
        this.currentPlayerUsername = currentPlayerUsername;
        notifyObservers();
    }

    public Boolean getTurn() {
        return isTurn;
    }

    public void setTurn(Boolean turn) {
        isTurn = turn;
    }

    public String getNextPlayerUUID() {
        return nextPlayerUUID;
    }

    public void setNextPlayerUUID(String nextPlayerUUID) {
        this.nextPlayerUUID = nextPlayerUUID;
    }

    @Override
    public void registerObserver(PlayerTurnObverser observer) {
        playerTurnObversers.add(observer);
    }

    @Override
    public void unregisterObserver(PlayerTurnObverser observer) {
        playerTurnObversers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (PlayerTurnObverser playerTurnObverser : playerTurnObversers) {
            playerTurnObverser.update(this);
        }
    }
}
