package Service;

import Model.GameState;
import Model.Player;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseService {
    private Firestore db;
    private final String PRIVATE_KEY = "firebase_privatekey.json";

    public FirebaseService() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Initialize Firestore connection
                FileInputStream serviceAccount = new FileInputStream(PRIVATE_KEY);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("database_url")
                        .build();
                FirebaseApp.initializeApp(options);
            }
            this.db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addPlayer(String code, Player player) throws Exception {
        DocumentReference lobbyReference = getLobbyReference(code);
        Map<String, Object> roomData = getLobbyData(code);

        if (roomData == null) {
            throw new Exception("Room not found");
        }

        if ((Boolean) roomData.get("ongoing")) {
            throw new Exception("Room is ongoing");
        }

        ArrayList<Player> players = (ArrayList<Player>) roomData.get("players");

        if (players.size() == 5) {
            throw new Exception("Room is full");
        }

        players.add(player);
        lobbyReference.update("players", FieldValue.arrayUnion(player));
    }


    private Map<String, Object> getLobbyData(String code) {
        try {
            return db.collection("rooms").document(code).get().get().getData();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Player> getPlayersFromLobby(String code) {
        ArrayList<Player> players = null;
        try {
            players = getLobbyReference(code).get().get().toObject(GameState.class).getPlayers();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return players;
    }

    public Player getPlayerFromLobby(String code, String player_uuid) {
        ArrayList<Player> players = getPlayersFromLobby(code);
        for (Player player: players) {
            if (player.getUUID().equals(player_uuid)) {
                return player;
            }
        }
        return null;
    }
    
    public void removePlayer(String roomCode, String player_uuid) {
        DocumentReference lobbyReference = getLobbyReference(roomCode);
        ArrayList<Player> players = getPlayersFromLobby(roomCode);
        Player player = getPlayerFromLobby(roomCode, player_uuid);
        lobbyReference.update("players", FieldValue.arrayRemove(player));

        if (players.size() == 1) {
            lobbyReference.delete();
        }
    }

    /*
    * Return true if the lobby is generated
    * */
    public boolean addLobby(String code, Player player) {
        try {
            // Get all documents from collection
            CollectionReference rooms = db.collection("rooms");
            List<QueryDocumentSnapshot> all_rooms = rooms.get().get().getDocuments();
            if (!all_rooms.contains(code)) {
                ArrayList<Player> players = new ArrayList<>();
                players.add(player);
                GameState gameState = new GameState("Waiting for the host to start the game", false, players);
                rooms.document(code).set(gameState);
                return true;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void updateMessageOfLobby(String code, String message) {
        DocumentReference lobbyReference = getLobbyReference(code);
        lobbyReference.update("message", message);
    }
    
    public void updateOngoingOfLobby(String code, Boolean isOngoing) {
        DocumentReference lobbyReference = getLobbyReference(code);
        lobbyReference.update("ongoing", isOngoing);
    }

    public GameState getGameStateOfLobby(String code) {
        try {
            return getLobbyReference(code).get().get().toObject(GameState.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateGameStateOfLobby(String code, GameState gameState) {
        DocumentReference lobbyReference = getLobbyReference(code);
        lobbyReference.set(gameState);
    }

    public DocumentReference getLobbyReference(String code) {
        return db.collection("rooms").document(code);
    }
}
