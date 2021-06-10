package Model;

import Observers.BannerObservable;
import Observers.BannerObserver;

import java.util.ArrayList;

public class PlayerBanner implements BannerObservable {
    private ArrayList<Player> players;

    private final ArrayList<BannerObserver> bannerObservers = new ArrayList<>();

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
        notifyObservers();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public void registerObserver(BannerObserver observer) {
        bannerObservers.add(observer);
    }

    @Override
    public void unregisterObserver(BannerObserver observer) {
        bannerObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (BannerObserver bannerObserver : bannerObservers) {
            bannerObserver.update(this);
        }
    }
}
