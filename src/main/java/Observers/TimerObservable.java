package Observers;

public interface TimerObservable {
    void registerObserver(TimerObserver observer);

    void unregisterObserver(TimerObserver observer);

    void notifyObservers();
}
