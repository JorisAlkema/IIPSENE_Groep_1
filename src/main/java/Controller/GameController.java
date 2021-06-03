package Controller;

import App.MainState;
import Model.City;
import Model.Player;
import Model.Route;
import Model.TrainCardDeck;
import Service.FirebaseService;
import Service.GameSetupService;
import Service.Observable;
import Service.Observer;
import View.CardView;
import View.GameView;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.util.*;

public class GameController implements Observable {
    private String timerText;
    private ArrayList<Observer> observers = new ArrayList<>();

    private ArrayList<Player> players;

    private int turnCount = 0;

    private int seconds;
    private Timer timer;

    private GameView gameView;

    public GameController(GameView gameView) {
        this.gameView = gameView;
        initGame();
    }

    public void initGame() {
        MainState.primaryStage.setOnCloseRequest(event -> timer.cancel());

        gameView.setRight(new CardView());

        players = MainState.firebaseService.getAllPlayers(MainState.roomCode);

        startTurn(getCurrentPlayer());
    }

    public Player getCurrentPlayer() {
        if (turnCount == 0) {
            return players.get(0);
        } else {
            return players.get(turnCount % players.size());
        }
    }

    private void startTurn(Player player) {
        player.setTurn(true);
        countdownTimer();
    }

    private void endTurn(Player player) {
        player.setTurn(false);
        timer.cancel();
        turnCount++;
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
                    endTurn(getCurrentPlayer());
                    timer.cancel();
                    startTurn(getCurrentPlayer());
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

    public void stopTimer() {
        timer.cancel();
    }

    public void setTimerText(String timerText) {
        this.timerText = timerText;
        Platform.runLater(() -> {
            notifyAllObservers(this.timerText);
        });
    }

    private String timerFormat(int seconds) {
        int minutes = (int) Math.floor(seconds / 60.0);
        int displaySeconds = (seconds % 60);
        return String.format("%d:%02d", minutes, displaySeconds);
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
    public void notifyAllObservers(Object o) {
        for (Observer observer : observers) {
            observer.update(this, o);
        }
    }
}
