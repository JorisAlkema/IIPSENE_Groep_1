package Observers;

public interface TurnTimerObservable {
    void registerObserver(TurnTimerObserver observer);

    void unregisterObserver(TurnTimerObserver observer);

    void notifyObservers();
}
