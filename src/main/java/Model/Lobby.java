package Model;

import App.MainState;
import Service.FirebaseService;
import Service.Observable;
import Service.Observer;
import View.MainMenuView;
import com.google.cloud.firestore.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

// Players can join together in a Lobby before they start the game. This means a Lobby is basically a collection of
// Players and a shared roomCode. Once enough players are ready, the host can start the game.
// Possible methods: join, leave, start, (changeHost?), ..
public class Lobby implements Observable {
    private ArrayList<Observer> observers = new ArrayList<>();

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
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(Object o, String type) {
        for (Observer observer : observers) {
            observer.update(this, o, type);
        }
    }
}
