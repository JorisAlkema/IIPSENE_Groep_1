package Model;

import Observers.LobbyObservable;
import Observers.LobbyObserver;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.ListenerRegistration;

import java.util.ArrayList;

// Players can join together in a Lobby before they start the game. This means a Lobby is basically a collection of
// Players and a shared roomCode. Once enough players are ready, the host can start the game.
// Possible methods: join, leave, start, (changeHost?), ..
public class Lobby implements LobbyObservable {
    private final ArrayList<LobbyObserver> observers = new ArrayList<>();

    private ListenerRegistration playerEventListener;

    public void setPlayerEventListener(ListenerRegistration playerEventListener) {
        this.playerEventListener = playerEventListener;
    }

    public ListenerRegistration getPlayerEventListener() {
        return playerEventListener;
    }

    /*
    Observer functions
    */

    @Override
    public void registerObserver(LobbyObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(LobbyObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(DocumentSnapshot documentSnapshot) {
        for (LobbyObserver observer : observers) {
            observer.update(documentSnapshot);
        }
    }
}
