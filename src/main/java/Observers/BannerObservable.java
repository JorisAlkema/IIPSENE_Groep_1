package Observers;

public interface BannerObservable {
    void registerObserver(BannerObserver observer);

    void unregisterObserver(BannerObserver observer);

    void notifyObservers();
}
