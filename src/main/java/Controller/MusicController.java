package Controller;

import Model.MusicPlayer;
import View.MusicPlayerView;

public class MusicController {
    private MusicPlayer musicPlayer = new MusicPlayer();
    private MusicPlayerView musicPlayerView = new MusicPlayerView(this);

    public MusicController() {
        musicPlayer.registerObserver(musicPlayerView);
    }

    public void toggleMusic() {
        musicPlayer.toggleMusic();
    }

}
