package Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import javafx.concurrent.Task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    // DEPRECATED
    public List<QueryDocumentSnapshot> fetchRoom(String code) {
        try {
            // Get all documents from collection
            ApiFuture<QuerySnapshot> future = db.collection(code).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            return documents;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Add a player to a room
    // If the room has 5 players -> reject
    // If game is ongoing -> reject
    public void addPlayer(Map<String, Object> player_data, String code) throws Exception {
        DocumentReference documentReference = db.collection(code).document("players");
        Map statusSnapshot = getDocumentData(code, "status");
        Map playerSnapshot = getDocumentData(code, "players");

        if (statusSnapshot == null || playerSnapshot == null) {
            throw new Exception("Room not found");
        }

        if ((Boolean) statusSnapshot.get("ongoing")) {
            throw new Exception("Room is ongoing");
        }

        if (playerSnapshot.size() == 5) {
            throw new Exception("Room is full");
        }

        documentReference.update(player_data);
    }

    public Map<String, Object> getDocumentData(String room_code, String path) {
        Map Snapshot = null;
        try {
            Snapshot = db.collection(room_code).document(path).get().get().getData();
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
        DocumentReference documentReference = db.collection(code).document("players");
        Map<String, Object> updates = new HashMap<>();
        updates.put(playerUUID, FieldValue.delete());
        // Update and delete the "capital" field in the document
        ApiFuture<WriteResult> writeResult = documentReference.update(updates);
    }

    // if lobby is created succesfully it return true
    // Try to generate a lobby using the provided code,
    // To add a host to the lobby the function needs a host_data which is generated with a function in Login
    public boolean addLobby(String code, Map<String, Object> host_data) {
        try {
            // Get all documents from collection
            ApiFuture<QuerySnapshot> future = db.collection(code).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (documents.size() == 0) {

                Map<String, Object> status = new HashMap<>();
                status.put("ongoing", false);

                db.collection(code).document("status").set(status);
                db.collection(code).document("players").set(host_data);

                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeLobby(String code) {

    }

    public void updatePlayerData() {

    }

    public DocumentReference getDocumentReference(String room_code, String path) {
        return db.collection(room_code).document(path);
    }
}
