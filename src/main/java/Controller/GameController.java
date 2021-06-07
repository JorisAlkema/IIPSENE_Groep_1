package Controller;

import App.MainState;
import Model.*;
import View.GameView;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import Observers.TimerObservable;
import Observers.TimerObserver;

import java.util.*;

public class GameController implements TimerObservable {
    private String timerText;
    private ArrayList<TimerObserver> observers = new ArrayList<>();

    private ArrayList<Player> players;
    private int playercount = 0;
    private int turnCount = 0;
    private String[] colors = {"GREEN","BLUE","PURPLE","RED","YELLOW"};

    private int seconds;
    private Timer timer;

    private GameView gameView;

    public GameController(GameView gameView) {
        this.gameView = gameView;
        MainState.primaryStage.setOnCloseRequest(event -> {
            try {
                stopTimer();
            } catch (Exception ignored) {}
        });
        initGame();
    }


    public void initGame() {
        playerColors();
        players = MainState.firebaseService.getPlayersFromLobby(MainState.roomCode);
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
        //setPlayerName(getCurrentPlayer().getName());
        countdownTimer();
    }

    public void endTurn(Player player) {
        player.setTurn(false);
        stopTimer();
        turnCount++;
    }

    public void playerColors(){
        //the hosts gives the other players their color
        if (MainState.firebaseService.getPlayerFromLobby(MainState.roomCode, MainState.player_uuid).getHost()) {
            GameState gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
            for(int i =0; MainState.firebaseService.getGameStateOfLobby(MainState.roomCode).getPlayers().size() > i; i++){
                gameState.getPlayers().get(i).setPlayerColor(colors[i]);
            }
            MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
        }
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
        Platform.runLater(this::notifyObservers);
    }

    private String formatTimer(int seconds) {
        int minutes = (int) Math.floor(seconds / 60.0);
        int displaySeconds = (seconds % 60);
        return String.format("%d:%02d", minutes, displaySeconds);
    }

    public ArrayList<StackPane> createOpponentViews() {
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

    /**
     * This method checks if the Cities on the given DestinationTicket have been connected by the given Player
     * It calls singleStep(), which uses recursive backtracking to find the path
     */
    public boolean isConnected(DestinationTicket ticket, Player player) {
        return singleStep(ticket.getFirstCity(), ticket.getSecondCity(), player);
    }

    /**
     * This method runs a single step in the pathfinding algorithm using backtracking.
     * It checks all neighbors of the currentCity, and if the player has built a Route from
     * currentCity to the neighbor, this method calls itself again, but now with the
     * neighbor City as the new currentCity. This way all possibilities to connect any two given
     * Cities are tried
     * @param currentCity City that we are at to check for a connection to destinationCity
     * @param destinationCity City that we are looking for a connection to
     * @param player Player that we are checking for if they have a connection between the two Cities
     * @return true if there is a connection from the initial currentCity to the destinationCity, false otherwise
     */
    private boolean singleStep(City currentCity, City destinationCity, Player player) {
        // Accept case - we found the destination city
        if (currentCity.equals(destinationCity)) {
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
    public void registerObserver(TimerObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void unregisterObserver(TimerObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (TimerObserver observer : observers) {
            observer.update(this.timerText);
        }
    }
}
