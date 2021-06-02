package View;

import Controller.MusicController;
import Service.Observable;
import Service.Observer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MusicPlayerView extends Pane implements Observer {
    private MusicController musicController;
    ImageView musicButton;

    public MusicPlayerView(MusicController musicController) {
        this.musicController = musicController;
        this.createMusicButton();
    }

    public void changeMusicButton(boolean musicIsPlaying) {
        Image image = new Image("images/volume-" + (musicIsPlaying ? "on" : "off") + ".png");
        this.musicButton.setImage(image);
    }

    public void createMusicButton() {
        Image image = new Image("images/volume-on.png");
        this.musicButton = new ImageView(image);
        getChildren().addAll(musicButton);
        musicButton.setOnMouseClicked((EventHandler< MouseEvent>) event -> musicController.toggleMusic());
    }

    @Override
    public void update(Observable observable, Object o) {
        this.changeMusicButton((boolean) o);
    }
}
