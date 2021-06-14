package View;

import Controller.MusicController;
import Observers.MusicObserver;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MusicPlayerView implements MusicObserver {
    private final MusicController musicController;
    public final ImageView musicButtonImageView;
    static MusicPlayerView musicPlayerView;

    private static final Image musicOnImage = new Image("images/icons/music-on.png");
    private static final Image musicOffImage = new Image("images/icons/music-off.png");

    public MusicPlayerView() {
        this.musicController = new MusicController();
        this.musicController.registerObserver(this);
        this.musicButtonImageView = new ImageView(musicOnImage);
        this.createMusicButton();
    }

    public static MusicPlayerView getInstance() {
        if (musicPlayerView == null) {
            musicPlayerView = new MusicPlayerView();
        }

        return musicPlayerView;
    }

    public void changeMusicButton(boolean isPlaying) {
        musicButtonImageView.setImage(isPlaying ? musicOnImage : musicOffImage);
    }

    public void createMusicButton() {
        musicButtonImageView.setId("musicbutton");
        musicButtonImageView.setOnMouseClicked(e -> musicController.toggleMusic());
    }

    public ImageView getMusicImageView() {
        return musicButtonImageView;
    }

    @Override
    public void update(boolean isPlaying) {
        this.changeMusicButton(isPlaying);
    }
}
