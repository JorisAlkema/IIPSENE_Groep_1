package Model;

import Observers.HandObservable;
import Observers.HandObserver;

import java.util.ArrayList;

public class HandModel implements HandObservable {
    static ArrayList<TrainCard> trainCardsInHand;
    static ArrayList<HandObserver> observers;
    static HandModel handModel;

    public HandModel(ArrayList<TrainCard> trainCards) {
        trainCardsInHand = trainCards;
        observers = new ArrayList<>();
    }

    public static HandModel getInstance(ArrayList<TrainCard> trainCards) {
        if (handModel == null) {
            handModel = new HandModel(trainCards);
        }
        return handModel;
    }

    public void setTrainCards(ArrayList<TrainCard> trainCards) {
        trainCardsInHand = trainCards;
        notifyObservers();
    }

    @Override
    public void registerObserver(HandObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(HandObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (HandObserver observer : observers) {
            observer.update(trainCardsInHand);
        }
    }
}
