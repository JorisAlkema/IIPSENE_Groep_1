package Controller;

import App.MainState;
import Model.Player;
import Service.Observable;
import Service.Observer;
import View.CardView;
import View.GameView;
import javafx.application.Platform;

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
        MainState.primaryStage.setOnCloseRequest(event -> timer.cancel());
        initGame();
    }

    public void initGame() {
        gameView.setRight(new CardView());

        players = MainState.firebaseService.getAllPlayers(MainState.roomCode);

        for (Player player : players) {
            player.setTurn(false);
        }

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
        setPlayerName(getCurrentPlayer().getName());
        countdownTimer();
    }

    public void endTurn(Player player) {
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
                    startTurn(getCurrentPlayer());
                }
            }
        }, delay, period);
    }

    private int setSeconds() {
        if (seconds == 0) {
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
            notifyAllObservers(this.timerText, "timer");
        });
    }

    private String timerFormat(int seconds) {
        int minutes = (int) Math.floor(seconds / 60.0);
        int displaySeconds = (seconds % 60);
        return String.format("%d:%02d", minutes, displaySeconds);
    }

    public void setPlayerName(String playerName) {
        Platform.runLater(() -> {
            notifyAllObservers(playerName, "playername");
        });
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
        for (Observer observer : observers) {
            observer.update(this, o, type);
        }
    }
    public GameController getGameController(){
        return GameController.this;
    }
}
