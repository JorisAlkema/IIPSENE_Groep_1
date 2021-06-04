package Model;

import java.util.ArrayList;

public class GameState {
    private String message;
    private Boolean ongoing;
    private ArrayList<Player> players;
    private ArrayList<TrainCard> openDeck;
    private ArrayList<TrainCard> closedDeck;

    public GameState(String message, Boolean ongoing, ArrayList<Player> players) {
        this.message = message;
        this.ongoing = ongoing;
        this.players = players;
    }

    public GameState() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Boolean getOngoing() {
        return ongoing;
    }

    public void setOngoing(Boolean ongoing) {
        this.ongoing = ongoing;
    }

    public ArrayList<TrainCard> getOpenDeck() {
        return openDeck;
    }

    public void setOpenDeck(ArrayList<TrainCard> openDeck) {
        this.openDeck = openDeck;
    }

    public ArrayList<TrainCard> getClosedDeck() {
        return closedDeck;
    }

    public void setClosedDeck(ArrayList<TrainCard> closedDeck) {
        this.closedDeck = closedDeck;
    }

    public Player getPlayer(String player_uuid) {
        for(Player x: this.players) {
            if (x.getUUID() == player_uuid) {
                return x;
            }
        }
        return null;
    }
}
