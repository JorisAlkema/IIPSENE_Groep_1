package Model;

import java.util.ArrayList;

// Players can join together in a Lobby before they start the game. This means a Lobby is basically a collection of
// Players and a shared roomCode. Once enough players are ready, the host can start the game.
// Possible methods: join, leave, start, (changeHost?), ..
public class Lobby {
    private final String roomCode;
    private ArrayList<Player> players;

    public Lobby(String roomCode, Player player) {
        this.roomCode = roomCode;
        this.players = new ArrayList<Player>();
        this.players.add(player);
    }

    public Lobby(String roomCode, ArrayList<Player> players) {
        this.roomCode = roomCode;
        this.players = players;
    }
}
