package Observers;

import com.google.cloud.firestore.DocumentSnapshot;

public interface LobbyObservable {
    void registerObserver(LobbyObserver observer);

    void unregisterObserver(LobbyObserver observer);

    void notifyObservers(DocumentSnapshot documentSnapshot);
}
