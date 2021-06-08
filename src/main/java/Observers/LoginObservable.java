package Observers;

public interface LoginObservable {
    public void registerObserver(LoginObserver loginObserver);

    public void unregisterObserver(LoginObserver loginObserver);

    public void notifyObservers(String message);
}
