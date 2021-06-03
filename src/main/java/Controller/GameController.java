package Controller;

import App.MainState;
import Model.*;
import Service.FirebaseService;
import Service.GameSetupService;
import Service.Observable;
import Service.Observer;
import View.CardView;
import View.GameView;
import javafx.application.Platform;

import java.util.*;
import java.util.stream.Collectors;

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

    // Main method used for testing. Can remove later
    public static void main(String[] args) {
        Player player = new Player();
        GameSetupService gameSetupService = new GameSetupService();
        ArrayList<Route> allRoutes = gameSetupService.getRoutes();
        ArrayList<Route> playerRoutes = new ArrayList<>();
        playerRoutes.add(allRoutes.get(0)); // Munchen Wien
        playerRoutes.add(allRoutes.get(1)); // Berlin Wien
//        playerRoutes.add(allRoutes.get(2)); // Frankfurt Berlin
        playerRoutes.add(allRoutes.get(3)); // Frankfurt Berlin 2
//        playerRoutes.add(allRoutes.get(4)); // Frankfurt Munchen
        playerRoutes.add(allRoutes.get(5)); // Frankfurt Essen
//        playerRoutes.add(allRoutes.get(6)); // Paris Frankfurt
//        playerRoutes.add(allRoutes.get(9)); // Amsterdam Frankfurt
//        playerRoutes.add(allRoutes.get(10)); // Bruxelles Amsterdam
//        playerRoutes.add(allRoutes.get(11)); // Paris Bruxelles
        playerRoutes.add(allRoutes.get(14)); // London Amsterdam
        playerRoutes.add(allRoutes.get(46)); // Amsterdam Essen

        player.setClaimedRoutes(playerRoutes);
        for (Route route : player.getClaimedRoutes()) {
            System.out.println("Player has route: " + route.getFirstCity() + "-" + route.getSecondCity());
        }
        ArrayList<DestinationTicket> tickets = gameSetupService.getDestinationTickets();
        System.out.println(tickets.get(25));
        GameController gameController = new GameController();

        System.out.println(gameController.isConnected(tickets.get(25), player));
    }

    // Empty constructor needed for testing. Can remove later
    public GameController() {}

    // This method checks if the Cities on the given DestinationTicket have been connected by the given Player
    // It calls singleStep(), which uses recursive backtracking to find the path
    public boolean isConnected(DestinationTicket ticket, Player player) {
        System.out.println(ticket.getFirstCity());
        System.out.println(ticket.getSecondCity());
        return singleStep(ticket.getFirstCity(), ticket.getSecondCity(), player);
    }

    // This method runs a single step in the backtracking pathfinding algorithm.
    // It checks all neighbors of the currentCity, and if the Player has built a Route from
    // currentCity to the neighbor, this method calls itself again, but now with the
    // neighbor City as the new currentCity. This way all possibilities to connect any two given cities are tried
    // Returns true if there is a connection from the initial currentCity to the destinationCity
    // and false otherwise
    private boolean singleStep(City currentCity, City destinationCity, Player player) {
        // Accept case - we found the destination city
        if (currentCity.equals(destinationCity)) {
            System.out.println("Found route to " + destinationCity + ":");
            return true;
        }
        // Reject case - we already visited this city
        if (currentCity.isVisited()) {
            return false;
        }
        // Backtracking step
        // Make a note that we visited this City, then try to go to each neighbor city
        currentCity.setVisited(true);
        for (City neighbor : currentCity.getNeighborCities()) {
            for (Route route : player.getClaimedRoutes()) {
                // If the player has built a route from currentCity to neighbor,
                // run the function again with neighbor as the new currentCity
                boolean connectedAToB = route.getFirstCity().equals(currentCity) && route.getSecondCity().equals(neighbor);
                boolean connectedBToA = route.getFirstCity().equals(neighbor) && route.getSecondCity().equals(currentCity);
                if (connectedAToB || connectedBToA) {
                    if (singleStep(neighbor, destinationCity, player)) {
                        System.out.println(currentCity + " " + neighbor);
                        return true;
                    }
                }
            }
        }
        // Dead end - this location can't be part of the solution
        // Unmark the location and go back to previous step
        currentCity.setVisited(false);
        return false;
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
}
