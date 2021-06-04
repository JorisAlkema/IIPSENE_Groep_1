package Controller;

import Model.MusicPlayer;
import View.MusicPlayerView;

public class MusicController {
    private MusicPlayer musicPlayer;
    private MusicPlayerView musicPlayerView;

    public MusicController(MusicPlayerView musicPlayerView) {
        this.musicPlayerView = musicPlayerView;
        this.musicPlayer = new MusicPlayer();
        this.musicPlayer.registerObserver(musicPlayerView);
    }

    public void toggleMusic() {
        this.musicPlayer.toggleMusic();
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    //
//    public void displayUsername() {
//        playerView.showUsername(
//                playerModel.getUsername()
//        );
//    }
//
//    public void displayPlayer() {
//        playerView.showPlayer(
//          playerModel.getX(),
//          playerModel.getY()
//        );
//    }
//
//    // In view class
//    public void showPlayer(int playerX, int playerY) {
//        this.ctx.draw(playerX, playerY, this.image)
//    }
}
