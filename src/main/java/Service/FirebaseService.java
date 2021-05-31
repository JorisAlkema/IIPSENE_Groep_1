package Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
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
    public void addPlayer(Map<String, Object> player_data, String code) throws Exception {
        DocumentReference documentReference = db.collection("rooms").document(code);
        Map<String, Object> snapShot = getDocumentData("rooms", code);

        if (snapShot == null) {
            throw new Exception("Room not found");
        }

        if ((Boolean) snapShot.get("ongoing")) {
            throw new Exception("Room is ongoing");
        }

        Map<String, Object> players = (Map<String, Object>) snapShot.get("players");

        if (players.size() == 5) {
            throw new Exception("Room is full");
        }

        String uuid = player_data.keySet().iterator().next();
        Object data = player_data.get(uuid);
        players.put(uuid, data);
        snapShot.put("players", players);
        documentReference.update(snapShot);
    }


    // Get document data of specifiek collections and document
    public Map<String, Object> getDocumentData(String collection, String document) {
        Map<String, Object> Snapshot = null;
        try {
            Snapshot = db.collection(collection).document(document).get().get().getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Snapshot;
    }

    // Remove a player using it's UUID and the roomcode.
    // It is possible to remove other players, but that is outside the scope.
    public void removePlayer(String playerUUID, String code) {
        DocumentReference documentReference = db.collection("rooms").document(code);
        Map<String, Object> snapShot = getDocumentData("rooms", code);
        Map<String, Object> players = (Map<String, Object>) snapShot.get("players");

        // if you were the last player remove the room
        if (players.size() == 1) {
            db.collection("rooms").document(code).delete();
        } else if (players != null) {
            players.remove(playerUUID);
            snapShot.put("players", players);
            documentReference.update(snapShot);
        }
    }

    // if lobby is created succesfully it return true
    // Try to generate a lobby using the provided code,
    // To add a host to the lobby the function needs a host_data which is generated with a function in Login
    public boolean addLobby(String code, Map<String, Object> host_data) {
        try {
            // Get all documents from collection
            CollectionReference rooms = db.collection("rooms");
            List<QueryDocumentSnapshot> all_rooms = rooms.get().get().getDocuments();
            if (!all_rooms.contains(code)) {
                Map<String, Object> data = new HashMap<String, Object>();
                Map<String, Object> players = new HashMap<String, Object>();

                String uuid = host_data.keySet().iterator().next();
                Object host = host_data.get(uuid);
                players.put(uuid, host);

                data.put("ongoing", false);
                data.put("message", "Waiting for the host to start the game");
                data.put("players", players);

                rooms.document(code).set(data);
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateMessageInLobby(String code, String message) {
        DocumentReference documentReference = db.collection("rooms").document(code);
        Map<String, Object> snapShot = getDocumentData("rooms", code);
        snapShot.put("message", message);
        documentReference.update(snapShot);
    }

    public void removeLobby(String code) {

    }

    public void updatePlayerData() {

    }

    // Get document reference for the eventlistener
    public DocumentReference getDocumentReference(String room_code, String path) {
        return db.collection(room_code).document(path);
    }
}
