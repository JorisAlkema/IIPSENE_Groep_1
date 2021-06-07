package Controller;

import App.MainState;
import Model.*;
import Service.GameSetupService;
import Service.Observable;
import Service.Observer;
import View.CardView;
import View.GameView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.*;

public class GameController implements Observable {
    private String timerText;
    private ArrayList<Observer> observers = new ArrayList<>();

    private ArrayList<Player> players;
    private int playercount = 0;
    private int turnCount = 0;

    private int seconds;
    private Timer timer;

    private GameView gameView;

    public GameController(GameView gameView) {
        this.gameView = gameView;
        MainState.primaryStage.setOnCloseRequest(event -> {
            try {
                timer.cancel();
            } catch (Exception ignored) {}
        });
        initGame();
    }



    public void initGame() {
        gameView.setRight(new CardView(this));

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
                    setTimerText(formatTimer(setSeconds()));
                } else if (seconds == 0) {
                    // Code that gets executed after the countdown has hit 0
                    setTimerText(formatTimer(setSeconds()));
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
        return formatTimer(setSeconds());
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

    private String formatTimer(int seconds) {
        int minutes = (int) Math.floor(seconds / 60.0);
        int displaySeconds = (seconds % 60);
        return String.format("%d:%02d", minutes, displaySeconds);
    }

    public void setPlayerName(String playerName) {
        Platform.runLater(() -> {
            notifyAllObservers(playerName);
        });
    }

    public ArrayList<StackPane> getPlayerCards() {
        ArrayList<StackPane> stackPanes = new ArrayList<>();
        ArrayList<ImageView> banners = new ArrayList<>();
        banners.add(new ImageView("images/player_banner_green.png"));
        banners.add(new ImageView("images/player_banner_blue.png"));
        banners.add(new ImageView("images/player_banner_purple.png"));
        banners.add(new ImageView("images/player_banner_red.png"));
        banners.add(new ImageView("images/player_banner_yellow.png"));

        for (int i = 0; i < players.size(); i++) {
            Text playerName = new Text("Player: " + players.get(i).getName());
            //String playerTrainCards = "Traincards: " + player.getTrainCards().size() + "\n";
            //String playerDestTickets = "Tickets: " + player.getDestinationTickets().size() + "\n";
            Text playerTrainCards = new Text("Traincards: 15");
            Text playerDestTickets = new Text("Tickets: 3");
            Text playerPoints = new Text("Points: " + players.get(i).getPoints());
            Text playerTrains = new Text("Trains: " + players.get(i).getTrains());
            playerName.getStyleClass().add("playerinfo");
            playerTrainCards.getStyleClass().add("playerinfo");
            playerDestTickets.getStyleClass().add("playerinfo");
            playerPoints.getStyleClass().add("playerinfo");
            playerTrains.getStyleClass().add("playerinfo");

            GridPane gridPane = new GridPane();
            gridPane.add(playerName, 0, 0, 2, 1);
            gridPane.add(playerTrainCards, 0, 1);
            gridPane.add(playerDestTickets, 1, 1);
            gridPane.add(playerPoints, 0, 2);
            gridPane.add(playerTrains, 1, 2);
            gridPane.setHgap(10);
            gridPane.setTranslateX(40);
            gridPane.setTranslateY(17);

            ImageView playerBanner = banners.get(i);
            playerBanner.setPreserveRatio(true);
            playerBanner.setFitHeight(100);

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(playerBanner, gridPane);

            stackPanes.add(stackPane);
        }
        return stackPanes;
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
    public void notifyAllObservers(Object o) {
        for (Observer observer : observers) {
            observer.update(this, o);
        }
    }
    public GameController getGameController(){
        return GameController.this;
    }
}
