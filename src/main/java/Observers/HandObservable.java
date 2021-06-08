package Observers;

public interface HandObservable {
    void registerObserver(HandObserver observer);

    void unregisterObserver(HandObserver observer);

    void notifyObservers();
}
