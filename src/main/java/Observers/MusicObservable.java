package Observers;

public interface MusicObservable {
    void registerObserver(MusicObserver observer);

    void unregisterObserver(MusicObserver observer);

    void notifyObservers();
}
