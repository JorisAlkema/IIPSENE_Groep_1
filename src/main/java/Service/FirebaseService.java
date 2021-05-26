package Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

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

    public FirebaseService() throws IOException {
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

    public void addPlayer(String playerUUID, Map<String, Object> data, String code) {
        DocumentReference documentReference = db.collection(code).document("players");

        Map<String, Object> players = new HashMap<>();
        players.put(playerUUID, data);

        ApiFuture<WriteResult> writeResult  = documentReference.update(players);
        try {
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(String playerUUID, String code) {
        DocumentReference documentReference = db.collection(code).document("players");
        Map<String, Object> updates = new HashMap<>();
        updates.put(playerUUID, FieldValue.delete());
        // Update and delete the "capital" field in the document
        ApiFuture<WriteResult> writeResult = documentReference.update(updates);
    }

    public void updatePlayerData() {

    }
}
