package Model;

import Service.Observable;
import Service.Observer;
import View.MusicPlayerView;

import java.util.ArrayList;

public class MusicPlayer implements Observable {
    private boolean isPlaying = true;
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    public void toggleMusic() {
        isPlaying = !isPlaying;
        this.notifyAllObservers(isPlaying);
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {

    }

    @Override
    public void notifyAllObservers(Object o) {
        for(Observer observer : observers) {
            observer.update( this, o);
        }
    }
}
