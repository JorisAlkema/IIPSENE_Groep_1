package Observers;

import Model.TrainCard;

import java.util.ArrayList;

public interface CardsObservable {
    void registerObserver(CardsObserver observer);

    void unregisterObserver(CardsObserver observer);

    void notifyObservers(ArrayList<TrainCard> openCards);
}
