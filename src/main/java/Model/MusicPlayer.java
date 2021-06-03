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
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    public void toggleMusic() {
        isPlaying = !isPlaying;
        playAudio(isPlaying);
        this.notifyAllObservers(isPlaying, "");
    }

    public void playAudio(boolean isPlaying) {
        Media media = new Media("https://www.chalitandu.nu/iipsene/MenuMusic.mp3");
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if(isPlaying) {
            mediaPlayer.setAutoPlay(true);
        }
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {

    }

    @Override
    public void notifyAllObservers(Object o, String type) {
        for(Observer observer : observers) {
            observer.update( this, o, "update");
        }
    }
}
