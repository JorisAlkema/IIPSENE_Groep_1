package Controller;

import Model.MusicPlayer;
import Observers.MusicObserver;

public class MusicController {
    private final MusicPlayer musicPlayer;

    public MusicController() {
        this.musicPlayer = MusicPlayer.getInstance();
    }

    public void toggleMusic() {
        this.musicPlayer.toggleMusic();
    }

    public void registerObserver(MusicObserver observer) {
        this.musicPlayer.registerObserver(observer);
    }

}
