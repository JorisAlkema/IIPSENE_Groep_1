package Controller;

import Model.Player;
import Model.PlayerBanner;
import Observers.BannerObserver;

import java.util.ArrayList;

public class PlayerBannerController {
    private PlayerBanner playerBanner = new PlayerBanner();

    public void updatePlayersArray(ArrayList<Player> players) {
        playerBanner.setPlayers(players);
    }
    public void registerObserver(BannerObserver bannerObserver) {
        playerBanner.registerObserver(bannerObserver);
    }
}
