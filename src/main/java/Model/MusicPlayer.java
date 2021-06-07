package Model;

import Service.Observable;
import Service.Observer;
import View.MusicPlayerView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayer implements Observable {
    private boolean isPlaying = true;
    private ArrayList<Observer> observers = new ArrayList<>();
    private Media media;
    private MediaPlayer mediaPlayer;

    public MusicPlayer() {
        media = new Media(new File("src/main/resources/music/europe.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void toggleMusic() {
        isPlaying = !isPlaying;
        playAudio(isPlaying);
        this.notifyAllObservers(isPlaying, "");
    }

    public void playAudio(boolean isPlaying) {
        if(isPlaying) {
            mediaPlayer.play();
            mediaPlayer.setAutoPlay(true);
        } else {
            // Mute, pause or stop? What is most logical
            mediaPlayer.pause();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(Object o, String type) {
        for(Observer observer : observers) {
            observer.update( this, o, "update");
        }
    }
}
