package Service;

import java.util.ArrayList;

public class Observable {

    private ArrayList<Observer> observers;

    public Observable() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {

    }

    public void deleteObserver(Observer observer) {

    }

    public void notifyObserver(Observer observer) {

    }

    public void notifyAllObservers() {
        for (Observer observer : observers) {
            notifyObserver(observer);
        }
    }
}