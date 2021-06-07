package View;

import Controller.MusicController;
import Service.Observable;
import Service.Observer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MusicPlayerView implements Observer {
    private MusicController musicController;
    private static final Image musicOnImage = new Image("images/music-on.png");
    private static final Image musicOffImage = new Image("images/music-off.png");
    public ImageView musicImageView;

    public MusicPlayerView() {
        this.musicController = new MusicController(this);
        this.musicImageView = new ImageView(musicOnImage);
        this.createMusicButton();
    }

    public void changeMusicButton() {
//        System.out.println(musicIsPlaying);
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
    public void update(Observable observable, Object o, String type) {
        this.changeMusicButton();
    }
}
