package Model;

import Observers.MusicObservable;
import Observers.MusicObserver;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

public class MusicPlayer implements MusicObservable {
    static MusicPlayer musicPlayer;
    private final MediaPlayer mediaPlayer;
    private MediaPlayer endGameMusicPlayer; // Instance variable to avoid garbage collection
    private boolean isPlaying = true;
    private final ArrayList<MusicObserver> observers = new ArrayList<>();

    public MusicPlayer() {
        Media media = new Media(String.valueOf(getClass().getClassLoader().getResource("music/europe.mp3")));
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public static MusicPlayer getInstance() {
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }
        return musicPlayer;
    }

    public void playEndMusic() {
        if(isPlaying) {
            mediaPlayer.pause();
        }

        Media media = new Media(String.valueOf(getClass().getClassLoader().getResource("music/victoryJingle.mp3")));
        endGameMusicPlayer = new MediaPlayer(media);
        endGameMusicPlayer.setOnEndOfMedia(() -> {
            if (isPlaying) {
                this.mediaPlayer.play();
            }
        });
        if (isPlaying) {
            endGameMusicPlayer.play();
        }
    }

    public void toggleMusic() {
        isPlaying = !isPlaying;
        playAudio(isPlaying);
        this.notifyObservers();
    }

    private void playAudio(boolean isPlaying) {
        if (isPlaying) {
            mediaPlayer.play();
            mediaPlayer.setAutoPlay(true);
        } else {
            mediaPlayer.pause();
        }
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
        for (MusicObserver observer : observers) {
            observer.update(isPlaying);
        }
    }
}
