package Model;

import Observers.HandObservable;
import Observers.HandObserver;

import java.util.ArrayList;

public class HandModel implements HandObservable {
    ArrayList<TrainCard> trainCards;
    private ArrayList<HandObserver> observers;

    public HandModel(ArrayList<TrainCard> trainCards) {
        this.trainCards = trainCards;
    }

    @Override
    public void registerObserver(HandObserver observer) {
        if (this.observers == null) {
            this.observers = new ArrayList<>();
        }
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(HandObserver observer) {
        if (this.observers == null) {
            this.observers = new ArrayList<>();
        }
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        if (this.observers == null) {
            return;
        }
        for (HandObserver observer : this.observers) {
            observer.update(this.trainCards);
        }
    }
}
