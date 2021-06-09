package Observers;

import com.google.cloud.firestore.DocumentSnapshot;

public interface LobbyObserver {
    void update(DocumentSnapshot documentSnapshot);
}
