package Model;

import java.util.*;
import Observers.LoginObservable;
import Observers.LoginObserver;

public class Login implements LoginObservable {
    private ArrayList<LoginObserver> observers = new ArrayList<>();
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
    public void registerObserver(LoginObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(LoginObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (LoginObserver observer : observers) {
            observer.update(message);
        }
    }
}
