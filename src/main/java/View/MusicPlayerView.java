package View;

import Controller.MusicController;
import Model.MusicPlayer;
import Service.Observable;
import Service.Observer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javax.swing.*;

public class MusicPlayerView extends VBox implements Observer {
    private MusicController musicController;
    public ImageView musicButton = new ImageView(new Image("images/music-on.png"));

    public MusicPlayerView(MusicController musicController) {
        this.musicController = musicController;
        this.createMusicButton();
        getChildren().add(musicButton);
    }

    public void changeMusicButton(boolean isPlaying) {
        Image image = new Image("images/music-" + (isPlaying ? "on" : "off") + ".png");
        musicButton.setImage(image);
    }

    public void createMusicButton() {
        musicButton.setId("musicbutton");
        musicButton.setOnMouseClicked(event -> {
            musicController.toggleMusic();
        });
    }


    @Override
    public void update(Observable observable, Object o, String type) {
        Platform.runLater(() -> changeMusicButton((boolean) o));
    }
}
