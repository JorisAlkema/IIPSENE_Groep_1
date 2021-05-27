package Service;

import java.util.ArrayList;

public class Observable {

    private ArrayList<Observer> observers;

    public Observable() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void deleteObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObserver(Observer observer, Object o) {
        observer.update(this, o);
    }

    public void notifyAllObservers(Object o) {
        for (Observer observer : observers) {
            notifyObserver(observer, o);
        }
    }
}