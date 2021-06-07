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
import java.util.concurrent.ExecutionException;

public class FirebaseService {
    private final String PRIVATE_KEY = "firebase_privatekey.json";
    private Firestore database;

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
            this.database = FirestoreClient.getFirestore();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayerToLobby(String code, Player player) throws Exception {
        DocumentReference lobbyReference = getLobbyReference(code);
        GameState gameState = getGameStateOfLobby(code);

        if (gameState == null) {
            throw new Exception("Room not found");
        }

        if (gameState.getOngoing()) {
            throw new Exception("Room is ongoing");
        }

        if (gameState.getPlayers().size() == 5) {
            throw new Exception("Room is full");
        }

        lobbyReference.update("players", FieldValue.arrayUnion(player));
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

    public ArrayList<Player> getPlayersFromLobby(String code) {
        return getGameStateOfLobby(code).getPlayers();
    }

    public Player getPlayerFromLobby(String code, String player_uuid) {
        for (Player player : getPlayersFromLobby(code)) {
            if (player.getUUID().equals(player_uuid)) {
                return player;
            }
        }
        return null;
    }

    public String addLobby(Player player) throws Exception {
        try {
            CollectionReference rooms = database.collection("rooms");
            List<QueryDocumentSnapshot> allRooms = rooms.get().get().getDocuments();

            String code = generateCode();
            while (allRooms.contains(code)) {
                code = generateCode();
            }

            ArrayList<Player> players = new ArrayList<>();
            players.add(player);

            GameState gameState = new GameState("Waiting for the host to start the game", false, players);
            rooms.document(code).set(gameState);

            return code;


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new Exception("Error adding Lobby");
        }
    }

    public void updateMessageOfLobby(String code, String message) {
        DocumentReference lobbyReference = getLobbyReference(code);
        lobbyReference.update("message", message);
    }

    public void updateOngoingOfLobby(String code, Boolean isOngoing) {
        DocumentReference lobbyReference = getLobbyReference(code);
        lobbyReference.update("ongoing", isOngoing);
    }

    private String generateCode() {
        return Integer.toString((int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000));
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
        return database.collection("rooms").document(code);
    }
}
