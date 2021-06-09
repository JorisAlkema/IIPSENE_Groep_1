package Model;

import Observers.MusicObservable;
import Observers.MusicObserver;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayer implements MusicObservable {
    private boolean isPlaying = true;
    private ArrayList<MusicObserver> observers = new ArrayList<>();
    static MusicPlayer musicPlayer;
    private Media media;
    private MediaPlayer mediaPlayer;

    public MusicPlayer() {
        media = new Media(new File("src/main/resources/music/europe.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static MusicPlayer getInstance() {
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }

        return musicPlayer;
    }

    public void toggleMusic() {
        isPlaying = !isPlaying;
        playAudio(isPlaying);
        this.notifyObservers();
    }

    public void playAudio(boolean isPlaying) {
        if(isPlaying) {
            mediaPlayer.play();
            mediaPlayer.setAutoPlay(true);
        } else {
            mediaPlayer.pause();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void registerObserver(MusicObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(MusicObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(MusicObserver observer : observers) {
            observer.update();
        }
    }
}
