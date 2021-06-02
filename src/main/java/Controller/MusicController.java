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
