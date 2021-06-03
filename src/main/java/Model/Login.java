package Model;

import App.MainState;
import Service.FirebaseService;
import Service.Observable;
import Service.Observer;
import View.LobbyView;
import View.MainMenuView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.*;

public class Login implements Observable {
    private ArrayList<Observer> observers = new ArrayList<>();
    private Boolean busy = false;

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
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
