package Observers;

public interface SystemMessageObservable {
    void registerObserver(SystemMessageObserver observer);

    void unregisterObserver(SystemMessageObserver observer);

    void notifyObservers();
}
