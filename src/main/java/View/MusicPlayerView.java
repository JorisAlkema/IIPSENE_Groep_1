package View;

import Controller.MusicController;
import Observers.MusicObserver;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MusicPlayerView implements MusicObserver {
    private final MusicController musicController;
    public final ImageView musicImageView;
    static MusicPlayerView musicPlayerView;
    private static final Image musicOnImage = new Image("images/music-on.png");
    private static final Image musicOffImage = new Image("images/music-off.png");

    public MusicPlayerView() {
        this.musicController = new MusicController(this);
        this.musicImageView = new ImageView(musicOnImage);
        this.createMusicButton();
    }

    public static MusicPlayerView getInstance() {
        if (musicPlayerView == null) {
            musicPlayerView = new MusicPlayerView();
        }

        return musicPlayerView;
    }

    public void changeMusicButton() {
        musicImageView.setImage(musicController.getMusicPlayer().isPlaying() ? musicOnImage : musicOffImage);
    }

    public void createMusicButton() {
        musicImageView.setId("musicbutton");
        musicImageView.setOnMouseClicked(event -> musicController.toggleMusic());
    }

    public ImageView getMusicImageView() {
        return musicImageView;
    }

    @Override
    public void update() {
        this.changeMusicButton();
    }
}
