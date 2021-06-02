package Model;

import java.util.ArrayList;

public class GameState {
    private String message;
    private Boolean ongoing;
    private ArrayList<Player> players;

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
}
