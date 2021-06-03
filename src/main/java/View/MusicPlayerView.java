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
    public ImageView musicButton = new ImageView(new Image("images/music-on.png"));

    public MusicPlayerView(MusicController musicController) {
        this.musicController = musicController;
        this.createMusicButton();
    }

    public void changeMusicButton(boolean musicIsPlaying) {
        System.out.println(musicIsPlaying);
        Image image = new Image("images/music-off.png");
        musicButton.setImage(image);
    }

    public void createMusicButton() {
        musicButton.setId("musicbutton");
        musicButton.setOnMouseClicked(event -> musicController.toggleMusic());
    }


    @Override
    public void update(Observable observable, Object o, String type) {
        this.changeMusicButton((boolean) o);
    }
}
