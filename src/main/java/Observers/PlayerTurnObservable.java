package Observers;

public interface PlayerTurnObservable {
    void registerObserver(PlayerTurnObverser observer);

    void unregisterObserver(PlayerTurnObverser observer);

    void notifyObservers();
}
