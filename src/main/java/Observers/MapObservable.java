package Observers;

public interface MapObservable {
    void registerObserver(MapObserver observer);

    void unregisterObserver(MapObserver observer);

    void notifyObservers();
}
