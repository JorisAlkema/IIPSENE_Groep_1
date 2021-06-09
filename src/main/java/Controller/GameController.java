package Controller;

import App.MainState;
import Model.*;
import Observers.CardsObserver;
import View.RoutePopUp;
import Observers.PlayerTurnObverser;
import com.google.cloud.firestore.ListenerRegistration;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import Observers.TurnTimerObserver;

import java.util.*;

public class GameController {
    private GameState gameState;
    private ListenerRegistration listenerRegistration;

    private PlayerTurnController playerTurnController = new PlayerTurnController();
    private CardsController cardsController = new CardsController();
    private MapController mapController = new MapController();
    private TurnTimerController turnTimerController = new TurnTimerController();



    public GameController() {
        MainState.primaryStage.setOnCloseRequest(event -> {
            try {
                turnTimerController.stopTimer();
                MainState.firebaseService.removePlayer(MainState.roomCode, MainState.player_uuid);
                // If nobody is in the room, delete it.
                if (MainState.firebaseService.getPlayersFromLobby(MainState.roomCode).size() == 0) {
                    MainState.firebaseService.getLobbyReference(MainState.roomCode).delete();
                }
            } catch (Exception ignored) {}
        });
        initGame();
    }

    /**
     * AVOID AS MANY UPDATES AND TRY TO PUSH UPDATES ALL AT ONCE!
     * 0. ATTACH LISTENER FOR GAME INITIALIZATION FROM THE HOST
     * 1. If host Generate Decks
     * 2. If host Generate Player colors
     * 3. Give first turn
     * // LOOP
     * 4. Wait for event to check turn / Do action and give turn
     * 5. ................/ Get 1 instance of GameState en modify it
     * 6. Show new data / Update data
     * 7. Check end game.
     * // END LOOP
     */

    public void initGame() {
        gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
        attachListener();
        // Init for host
        if (gameState.getPlayer(MainState.player_uuid).getHost()) {
            generateDecks();
            initializePlayerColors();
            giveFirstTurn();
            gameState.setLoadedByHost(true);
            updateGameState();
            System.out.println("Initialized");
        }
    }

    // Step 0
    public void attachListener() {
        listenerRegistration = MainState.firebaseService.getLobbyReference(MainState.roomCode).addSnapshotListener(((documentSnapshot, e) -> {
            Platform.runLater(() -> {
                System.out.println("INCOMING UPDATE");
                GameState incomingGameState = documentSnapshot.toObject(GameState.class);
                if (incomingGameState.isLoadedByHost()) {
                    // A player has leaved
                    if (incomingGameState.getPlayers().size() < gameState.getPlayers().size()) {
                        removeLeftPlayers(incomingGameState);
                    } else {
                        gameState = incomingGameState;
                        cardsController.notifyObservers(gameState.getOpenDeck());
                        // End old timer and Make time init timer
                        turnTimerController.resetTimer(this);
                    }

                    try {
                        playerTurnController.checkMyTurn(gameState);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        updateGameState();
                    }
                }
            });
        }));
    }

    // Step 1
    public void generateDecks() {
        ArrayList<TrainCard> closedCards = cardsController.generateClosedDeck();
        gameState.setOpenDeck(cardsController.generateOpenDeck(closedCards));
        gameState.setClosedDeck(closedCards);
    }

    // Step 2
    public void initializePlayerColors(){
        final ArrayList<String> PLAYER_COLORS = new ArrayList<>(Arrays.asList("GREEN","BLUE","PURPLE","RED","YELLOW"));
        for (Player player : gameState.getPlayers()) {
            player.setPlayerColor(PLAYER_COLORS.remove(0));
        }
    }

    // Step 3
    public void giveFirstTurn() {
        playerTurnController.start(gameState);
    }

    // Game
    // Step 1: Set Actions
    // Step 2: if Actions = 2, End turn and set next player turn = true
    // Step 3: update gameState
    public void updateGameState() {
        System.out.println("updateGameState");
        MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
    }


    // Actions
    // MAKE SURE ACTIONS ALWAYS HAVE incrementPlayerActionsTaken and checkNextTurn;
    public void pickClosedCard() {
        // Check if player turn
        System.out.println(playerTurnController.getTurn());
        if (playerTurnController.getTurn()) {
            TrainCard pickedClosedCard = cardsController.pickClosedCard(gameState);
            System.out.println("Picked Closed Card");
            addTrainCardToPlayerInventoryInGameState(pickedClosedCard);
            incrementPlayerActionsTaken();
            checkIfTurnIsOver();
        } else {
            System.out.println("IT'S NOT YOUR TURN");
        }
    }

    public void pickOpenCard(int index) {
        try {
            if (playerTurnController.getTurn()) {
                TrainCard pickedOpenCard = cardsController.pickOpenCard(gameState, index);
                System.out.println("Picked Open Card");
                addTrainCardToPlayerInventoryInGameState(pickedOpenCard);

                if (pickedOpenCard.getColor().equals("LOCO")) {
                    getLocalPlayerFromGameState().setActionsTaken(2);
                } else {
                    incrementPlayerActionsTaken();
                }
                checkIfTurnIsOver();
            } else {
                System.out.println("IT'S NOT YOUR TURN");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void buildRoute(Route route) {
        ArrayList<String> equalAmount = new ArrayList<>();
        String selectedColor;

        if (playerTurnController.getTurn()) {
            if (route.getColor().equals("GREY")) {
                for (Map.Entry<String, Integer> entry : getCurrentPlayer().getTrainCardsAsMap().entrySet()) {
                    if (entry.getValue() == route.getLength()) {
                        equalAmount.add(entry.getKey());
                    }
                }
                RoutePopUp routePopUp = new RoutePopUp(equalAmount);
                selectedColor = routePopUp.showRoutePopUp();
                mapController.claimRoute(route, selectedColor);
            }
            else {
                mapController.claimRoute(route, route.getColor());
            }
            incrementPlayerActionsTaken();
            checkIfTurnIsOver();
        } else {
            System.out.println("IT'S NOT YOUR TURN");
        }
    }

    public void endTurn() {
        if (playerTurnController.getTurn()) {
            getLocalPlayerFromGameState().setActionsTaken(2);
            checkIfTurnIsOver();
        }
    }

    // ===============================================================

    private void addTrainCardToPlayerInventoryInGameState(TrainCard trainCard) {
        getLocalPlayerFromGameState().addTrainCard(trainCard);
    }

    private Player getLocalPlayerFromGameState() {
        return gameState.getPlayer(MainState.player_uuid);
    }

    private Boolean isPlayerActionsTakenEquals2() {
        return (getLocalPlayerFromGameState().getActionsTaken() == 2);
    }

    private void incrementPlayerActionsTaken() {
        getLocalPlayerFromGameState().setActionsTaken(getLocalPlayerFromGameState().getActionsTaken() + 1);
    }

    public void registerCardsObserver(CardsObserver cardsObserver) {
        cardsController.registerObserver(cardsObserver);
    }

    public void registerTurnTimerObserver(TurnTimerObserver turnTimerObserver) {
        turnTimerController.registerObserver(turnTimerObserver);
    }

    public void registerPlayerTurnObserver(PlayerTurnObverser playerTurnObverser) {
        playerTurnController.registerObserver(playerTurnObverser);
    }

    private void checkIfTurnIsOver() {
        System.out.println("CHECK");
        if (isPlayerActionsTakenEquals2()) {
            // End turn
            System.out.println("NEXT TURN");
            getLocalPlayerFromGameState().setActionsTaken(0);
            playerTurnController.nextTurn(gameState);
            updateGameState();
        }
    }

    private void removeLeftPlayers(GameState incomingGameState) {
        ArrayList<String> remainingPlayers = new ArrayList<>();
        incomingGameState.getPlayers().forEach((n) -> remainingPlayers.add(n.getUUID()));
        gameState.getPlayers().removeIf(player -> !remainingPlayers.contains(player.getUUID()));
    }

    // ===============================================================

    // Do you have enough trains to build a route
    public void checkTrains() {
        if (getCurrentPlayer().getTrains() <= 2) {

        }
    }

    public Player getCurrentPlayer() {
        return playerTurnController.getCurrent(gameState);
    }

    public ArrayList<StackPane> createOpponentViews() {
        ArrayList<StackPane> stackPanes = new ArrayList<>();
        ArrayList<ImageView> banners = new ArrayList<>();
        banners.add(new ImageView("images/player_banner_green.png"));
        banners.add(new ImageView("images/player_banner_blue.png"));
        banners.add(new ImageView("images/player_banner_purple.png"));
        banners.add(new ImageView("images/player_banner_red.png"));
        banners.add(new ImageView("images/player_banner_yellow.png"));
        ArrayList<Player> players = MainState.firebaseService.getPlayersFromLobby(MainState.roomCode);
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
}
