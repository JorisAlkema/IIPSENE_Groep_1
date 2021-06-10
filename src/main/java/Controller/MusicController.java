package Controller;

import Model.MusicPlayer;
import View.MusicPlayerView;

public class MusicController {
    private MusicPlayer musicPlayer;
    private MusicPlayerView musicPlayerView;

    public MusicController(MusicPlayerView musicPlayerView) {
        this.musicPlayerView = musicPlayerView;
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
