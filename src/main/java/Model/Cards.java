package Model;

import Observers.CardsObservable;
import Observers.CardsObserver;

import java.util.ArrayList;

public class Cards implements CardsObservable {
    private ArrayList<CardsObserver> cardsObservers = new ArrayList<>();

    @Override
    public void registerObserver(CardsObserver observer) {
        cardsObservers.add(observer);
    }

    @Override
    public void unregisterObserver(CardsObserver observer) {
        cardsObservers.remove(observer);
    }

    @Override
    public void notifyObservers(ArrayList<TrainCard> openCards) {
        for (CardsObserver cardsObserver : cardsObservers) {
            cardsObserver.update(openCards);
        }
    }
}
