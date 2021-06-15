package Model;

import Observers.SystemMessageObservable;
import Observers.SystemMessageObserver;

import java.util.ArrayList;

public class SystemMessage implements SystemMessageObservable {
    private ArrayList<SystemMessageObserver> observers = new ArrayList<>();
    private String message;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        notifyObservers();
    }

    @Override
    public void registerObserver(SystemMessageObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(SystemMessageObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (SystemMessageObserver observer : observers) {
            observer.update(this);
        }
    }


}
