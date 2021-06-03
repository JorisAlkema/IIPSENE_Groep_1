package Model;

import Service.Observable;
import Service.Observer;
import View.MusicPlayerView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer implements Observable {
    private boolean isPlaying = true;
    private Clip clip;
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    public MusicPlayer() {
        playAudio();
    }

    public void toggleMusic() {
        isPlaying = !isPlaying;
        playAudio();
        this.notifyAllObservers(isPlaying, "");
    }

    public void playAudio() {
        if(isPlaying && (clip == null)) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("../music/MenuMusic.wav"));
                clip = AudioSystem.getClip();
                clip.setFramePosition(0);
                clip.open(audioInputStream);

                clip.start();
                clip.addLineListener(new LineListener() {
                    public void update(LineEvent myLineEvent) {
                        if (myLineEvent.getType() == LineEvent.Type.STOP) {
                            clip.close();
                            clip = null;
                            isPlaying = false;
                            playAudio();
                        }
                    }
                });
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        } else {
            clip.stop();
        }
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
            observer.update( this, o, "");
        }
    }
}
