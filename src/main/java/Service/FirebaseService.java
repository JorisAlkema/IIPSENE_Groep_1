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
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws IOException {
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
    }

    // Add a player to a room
    // If the room has 5 players -> reject
    // If game is ongoing -> reject
    public void addPlayer(String code, Player player) throws Exception {
        DocumentReference roomReference = getRoomReference(code);
        Map<String, Object> roomData = getRoomData(code);

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
        roomReference.update("players", FieldValue.arrayUnion(player));
    }

    // if = null room doesnt exists, used for addPlayer
    private Map<String, Object> getRoomData(String roomCode) {
        Map<String, Object> Snapshot = null;
        try {
            Snapshot = db.collection("rooms").document(roomCode).get().get().getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Snapshot;
    }

    // get all players of room
    public ArrayList<Player> getAllPlayers(String roomCode) {
        ArrayList<Player> players = null;
        try {
            players = getRoomReference(roomCode).get().get().toObject(GameState.class).getPlayers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return players;
    }

    public Player getPlayer(String roomCode, String player_uuid) {
        ArrayList<Player> players = getAllPlayers(roomCode);
        for (Player player: players) {
            if (player.getUUID() == player_uuid) {
                return player;
            }
        }
        return null;
    }

    // Remove a player using it's UUID and the roomcode.
    // It is possible to remove other players, but that is outside the scope.
    // If you are the last player in the room and this function is called, the room will get deleted
    public void removePlayer(String roomCode, String player_uuid) {
        DocumentReference roomReference = getRoomReference(roomCode);
        ArrayList<Player> players = getAllPlayers(roomCode);
        Player player = getPlayer(roomCode, player_uuid);
        roomReference.update("players", FieldValue.arrayRemove(player));

        if (players.size() == 1) {
            roomReference.delete();
        }
    }

    // if lobby is created succesfully it return true
    // Try to generate a lobby using the provided code,
    // To add a host to the lobby the function needs a host_data which is generated with a function in Login
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update message in the lobby
    public void updateMessageInLobby(String code, String message) {
        DocumentReference documentReference = getRoomReference(code);
        documentReference.update("message", message);
    }

    // Update Roomdata in the lobby
    public void updateOngoing(String code, Boolean isOngoing) {
        DocumentReference documentReference = getRoomReference(code);
        documentReference.update("ongoing", isOngoing);
    }

    public GameState getGameState(String code) {
        try {
            return getRoomReference(code).get().get().toObject(GameState.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateGameState(String code, GameState gameState) {
        DocumentReference documentReference = getRoomReference(code);
        documentReference.set(gameState);
    }

    public void updatePlayerData(String code, Player player) {
        DocumentReference roomReference = getRoomReference(code);
        ArrayList<Player> players = getAllPlayers(code);

        int index = 0;
        for (Player playerFirebase : players) {
            if (playerFirebase.getUUID() == player.getUUID()) {
                players.set(index, player);
                break;
            }
            index++;
        }

        roomReference.update("players", players);
    }

    // Get document reference for the eventlistener
    public DocumentReference getRoomReference(String room_code) {
        return db.collection("rooms").document(room_code);
    }
}
