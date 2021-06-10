package Controller;

import Model.MusicPlayer;
import View.MusicPlayerView;

public class MusicController {
    private final MusicPlayer musicPlayer;

    public MusicController(MusicPlayerView musicPlayerView) {
        this.musicPlayer = MusicPlayer.getInstance();
        this.musicPlayer.registerObserver(musicPlayerView);
    }

    public void toggleMusic() {
        this.musicPlayer.toggleMusic();
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

}
