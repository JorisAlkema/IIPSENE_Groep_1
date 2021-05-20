package Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class Firebase {

    // Testing Firebase!

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        final String PRIVATEKEY = "firebase_privatekey.json";

        FileInputStream serviceAccount =
                new FileInputStream(PRIVATEKEY);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("database_url")
                .build();

        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        Firebase app = new Firebase();

        // insert/ update
		app.update(db);

		// get
        app.getQuoteFromFirestore(db);

		// delete
		// app.deleteFromFirestore(db);
    }

    public void deleteFromFirestore(Firestore db) throws InterruptedException, ExecutionException {
        DocumentReference docRef = db.collection("firebaseTest").document("test_document");
        ApiFuture<WriteResult> future = docRef.delete();
        WriteResult result = future.get();

        System.out.println("Successfully deleted at: " + future.get().getUpdateTime());
    }

    public void getQuoteFromFirestore(Firestore db) throws InterruptedException, ExecutionException {
        DocumentReference docRef = db.collection("firebaseTest").document("test_document");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if(document.exists()) {
            String quote = (String) document.get("Tim");
            System.out.println(quote);
        } else {
            System.out.println("No such document.");
        }
    }

    public void update(Firestore db) throws IOException, InterruptedException, ExecutionException {
        // Insert & Update
        // If the document doesn't exist it will be created.
        // If the document already exists it will be created.

        HashMap<String, String> quote = getSomethingToInsert();

        ApiFuture<WriteResult> future = db.collection("firebaseTest")
                .document("joris_document")
                .set(quote);

        System.out.println("Successfully updated at: "
                + future.get().getUpdateTime());
    }

    public HashMap<String, String> getSomethingToInsert() throws IOException {
        HashMap<String, String> quoteHashMap = new HashMap<String, String>();
        quoteHashMap.put("Joris", "I also like trains.");

        return quoteHashMap;
    }
}
