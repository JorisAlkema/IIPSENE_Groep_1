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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MusicPlayerView extends Pane implements Observer {
    private MusicController musicController;
    private ImageView musicButton = new ImageView(new Image("images/music-on.png"));

    public MusicPlayerView(MusicController musicController) {
        this.musicController = musicController;
//        this.createMusicButton();
    }

    public void changeMusicButton(boolean musicIsPlaying) {
        System.out.println(musicIsPlaying);
        Image image = new Image("images/music-" + (musicIsPlaying ? "on" : "off") + ".png");
        System.out.println("images/music-" + (musicIsPlaying ? "on" : "off") + ".png");
        System.out.println(this.musicButton);
        this.musicButton = new ImageView(image);
    }

    public VBox createMusicButton() {
        this.musicButton.setId("musicbutton");
        VBox musicButtonContainer = new VBox(10);
        musicButtonContainer.setAlignment(Pos.BOTTOM_RIGHT);
        musicButtonContainer.setPadding(new Insets(20));
        musicButtonContainer.getChildren().add(this.musicButton);
        this.musicButton.setOnMouseClicked(event -> musicController.toggleMusic());
        return musicButtonContainer;

    }

    @Override
    public void update(Observable observable, Object o) {
        this.changeMusicButton((boolean) o);
    }
}
