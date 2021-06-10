package Observers;

import Model.TrainCard;

public interface BannerObservable {
    void registerObserver(CardsObserver observer);

    void unregisterObserver(CardsObserver observer);

    void notifyObservers(String playerInfo);
}
