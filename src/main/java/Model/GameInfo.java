package Model;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.*;

public class GameInfo {

    private List<Observer> observers = new ArrayList<>();
    private String timerText;

    /* 'REAL' ARRAYLIST GETS GENERATED IN THE LOBBY
    FINAL ARRAYLIST WILL BE PULLED FROM FIREBASE */
    private ArrayList<Player> players = new ArrayList<Player>();
    private int playerCount = players.size();

    private int turnCount = 0;

    static int seconds;
    static Timer timer;

    public GameInfo(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> timer.cancel());
    }

    public void initGame() {
        setTurn(getPlayer());
    }

    private Player getPlayer() {
        if (turnCount == 0) {
            return players.get(0);
        } else {
            return players.get(turnCount % playerCount);
        }
    }

    private void setTurn(Player player) {
        countdownTimer();
    }

    public void countdownTimer() {
        timer = new Timer();
        int delay = 1000;
        int period = 1000;

        // Increase time by 1, since 0:00 is counted as the final second
        seconds = 90 + 1;

        // Schedules the timer for repeated fixed-rate execution, beginning after the specified delay
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (seconds > 0 ) {
                    setTimerText(timerFormat(setSeconds()));
                } else if (seconds == 0) {
                    // Code that gets executed after the countdown has hit 0

                    setTimerText(timerFormat(setSeconds()));
                    turnCount++;
                }
            }
        }, delay, period);
    }

    private int setSeconds() {
        if (seconds == 0) {
            timer.cancel();
            return seconds;
        }
        return --seconds;
    }

    public String getTimer() {
        return timerFormat(setSeconds());
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void setTimerText(String timerText) {
        this.timerText = timerText;
        Platform.runLater(() -> {
            for (Observer observer : this.observers) {
                observer.update(this.timerText);
            }
        });
    }

    private String timerFormat(int timer) {
        int minutes = (int) Math.floor(timer / 60);
        int seconds = timer % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}
