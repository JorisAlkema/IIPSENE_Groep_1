package Observers;

public interface LoginObservable {
    void registerObserver(LoginObserver loginObserver);

    void unregisterObserver(LoginObserver loginObserver);

    void notifyObservers(String message);
}
