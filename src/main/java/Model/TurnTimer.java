package Model;

import Controller.GameController;
import Observers.TurnTimerObservable;
import Observers.TurnTimerObserver;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TurnTimer implements TurnTimerObservable {
    private ArrayList<TurnTimerObserver> turnTimerObservers = new ArrayList<>();
    private Timer timer = new Timer();

    private String timerText;

    private int seconds;
    private final int DELAY = 1000;
    private final int PERIOD = 1000;

    public void newCountDownTimer(GameController gameController) {
        timer = new Timer();

        // Increase time by 1, since 0:00 is counted as the final second
        seconds = 90 + 1;

        // Schedules the timer for repeated fixed-rate execution, beginning after the specified delay
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (seconds > 0 ) {
                    updateTimerTextInGameView();
                } else if (seconds == 0) {
                    System.out.println("TIMER ENDED");
                    gameController.endTurn();
                    // Code that gets executed after the countdown has hit 0
                    updateTimerTextInGameView();
                }
            }
        }, DELAY, PERIOD);
    }

    private int updateSeconds() {
        return --seconds;
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void updateTimerTextInGameView() {
        this.timerText = formatTimer(updateSeconds());
        Platform.runLater(this::notifyObservers);
    }

    private String formatTimer(int seconds) {
        int minutes = (int) Math.floor(seconds / 60.0);
        int displaySeconds = (seconds % 60);
        return (seconds < 0) ? "0:00" : String.format("%d:%02d", minutes, displaySeconds);
    }

    @Override
    public void registerObserver(TurnTimerObserver observer) {
        turnTimerObservers.add(observer);
    }

    @Override
    public void unregisterObserver(TurnTimerObserver observer) {
        turnTimerObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (TurnTimerObserver turnTimerObserver : turnTimerObservers) {
            turnTimerObserver.update(timerText);
        }
    }
}
