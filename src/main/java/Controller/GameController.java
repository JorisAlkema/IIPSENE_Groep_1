package Controller;

import App.MainState;
import Model.*;
import Observers.*;
import Service.GameSetupService;
import View.DestinationPopUp;
import View.EndGameView;
import View.GameView;
import View.RoutePopUp;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.firebase.messaging.Message;
import javafx.application.Platform;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameController {
    private GameState gameState;
    private ListenerRegistration listenerRegistration;

    private final PlayerTurnController playerTurnController = new PlayerTurnController();
    private final CardsController cardsController = new CardsController();
    private final MapController mapController = MapController.getInstance();
    private final TurnTimerController turnTimerController = new TurnTimerController();
    private final PlayerBannerController bannerController = new PlayerBannerController();
    private final SystemMessage systemMessage = new SystemMessage();

    private final GameSetupService gameSetupService = GameSetupService.getInstance();

    private DestinationPopUp destinationPopUp;
    private RoutePopUp routePopUp;

    private boolean firstTurn = true;

    private boolean lastRound = false;
    private boolean lastActionTaken = false;

    public GameController() {
        mapController.setCardsController(cardsController);

        MainState.primaryStage.setOnCloseRequest(event -> {
            try {
                turnTimerController.stopTimer();
                MainState.firebaseService.removePlayer(MainState.roomCode, MainState.player_uuid);
                // If nobody is in the room, delete it.
                if (MainState.firebaseService.getPlayersFromLobby(MainState.roomCode).size() == 0) {
                    MainState.firebaseService.getLobbyReference(MainState.roomCode).delete();
                }
            } catch (Exception ignored) {
            }
        });
        initGame();
    }

    /**
     * 0. ATTACH LISTENER FOR GAME INITIALIZATION FROM THE HOST
     * 1. If host Generate Decks.
     * 2. If host Generate Player colors.
     * 3. Give first turn.
     * // LOOP
     * 4. Wait for event to check turn / Do action and give turn.
     * 5. ................/ Get 1 instance of GameState en modify it.
     * 6. Show new data / Update data.
     * 7. Check end game.
     * // END LOOP
     **/

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
                if (incomingGameState != null && incomingGameState.isLoadedByHost()) {
                    if (!incomingGameState.getOngoing()) {
                        endGame();
                        return;
                    }
                    // A player has leaved
                    if (incomingGameState.getPlayers().size() < gameState.getPlayers().size()) {
                        removeLeftPlayers(incomingGameState);
                        bannerController.updatePlayersArray(gameState.getPlayers());
                        if (gameState.getPlayers().size() == 1) {
                            endGame();
                        }
                    } else {
                        gameState = incomingGameState;
                        // Check trains for all players
                        checkTrains();
                        cardsController.notifyObservers(gameState.getOpenDeck());
                        bannerController.updatePlayersArray(gameState.getPlayers());
                        mapController.redrawRoutes(gameState.getPlayers());

                        // End old timer and Make time init timer
                        turnTimerController.resetTimer(this);
                    }

                    try {
                        playerTurnController.checkMyTurn(gameState);
                        if (playerTurnController.getTurn()) {
                            checkEndGame();
                            systemMessage.setMessage("It's your turn.");
                        } else {
                            systemMessage.setMessage("It's " + getCurrentPlayer().getName() + "'s turn.");
                        }

                        if (firstTurn && playerTurnController.getTurn()) {
                            firstTurn = false;
                            for (int i = 0; i < 4; i++) {
                                TrainCard pickedClosedCard = cardsController.pickClosedCard(gameState);
                                addTrainCardToPlayerInventoryInGameState(pickedClosedCard);
                            }

                            if (destinationPopUp == null) {
                                destinationPopUp = new DestinationPopUp(gameState);
                                destinationPopUp.showAtStartOfGame(gameState, this);
                                endTurn();
                            }
                        }
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
        gameState.setDestinationDeck(gameSetupService.getDestinationTickets());
    }

    // Step 2
    public void initializePlayerColors() {
        final ArrayList<String> PLAYER_COLORS = new ArrayList<>(Arrays.asList("GREEN", "BLUE", "PURPLE", "RED", "YELLOW"));
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
            systemMessage.setMessage("You cannot pick a card at this time.");
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
                systemMessage.setMessage("You cannot pick a card at this time.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void buildRoute(Route route) {
        String selectedColor = null;
        String isBuilt;


        if (routePopUp == null) {
            if (playerTurnController.getTurn() && getLocalPlayerFromGameState().getActionsTaken() == 0) {
                if (route.routeLength() <= getLocalPlayerFromGameState().getTrains()) {
                    if (route.getColor().equals("GREY")) {
                        selectedColor = pickColorForGreyRoute(route);
                        isBuilt = mapController.claimRoute(route, selectedColor);
                    } else {
                        isBuilt = mapController.claimRoute(route, route.getColor());
                    }
                    systemMessage.setMessage(isBuilt);
                    if (isBuilt.equals("route has been built!")) {
                        givePointForRouteSize(route.routeLength());
                        endTurn();
                    } else if (isBuilt.equals("not enough cards for tunnels")) {
                        endTurn();
                    }
                } else {
                    systemMessage.setMessage("It's not your turn, or you already drew a TrainCard this turn.");
                }
            }
        } else {
            systemMessage.setMessage("Choose a card to build grey route first before taking another action");
        }
    }

    private String pickColorForGreyRoute(Route route) {
        ArrayList<TrainCard> trainCards = getLocalPlayerFromGameState().getTrainCards();
        int locosInHand = 0;
        for (TrainCard trainCard : trainCards) {
            if (trainCard.getColor().equals("LOCO")) {
                locosInHand++;
            }
        }

        ArrayList<String> possibleColors = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : getLocalPlayerFromGameState().trainCardsAsMap().entrySet()) {
            boolean enoughPureLocos = entry.getKey().equals("LOCO") && entry.getValue() >= route.routeLength();
            boolean enoughCardsIncludingLocos = !entry.getKey().equals("LOCO") && entry.getValue() + locosInHand >= route.routeLength();
            if (enoughPureLocos || enoughCardsIncludingLocos) {
                possibleColors.add(entry.getKey());
            }
        }

        if (possibleColors.size() == 0) {
            return null;
        }

        routePopUp = new RoutePopUp(possibleColors);
        return routePopUp.showRoutePopUp();
    }

    private void givePointForRouteSize(int routeLength) {
        switch (routeLength) {
            case 1:  getLocalPlayerFromGameState().incrementPoints(1); break;
            case 2: getLocalPlayerFromGameState().incrementPoints(2); break;
            case 3: getLocalPlayerFromGameState().incrementPoints(4); break;
            case 4: getLocalPlayerFromGameState().incrementPoints(7); break;
            case 6: getLocalPlayerFromGameState().incrementPoints(15); break;
            case 8: getLocalPlayerFromGameState().incrementPoints(21); break;
            default: getLocalPlayerFromGameState().incrementPoints(0); break;
        }
    }

    public void showDestinationCardsPopUp() {
        if (playerTurnController.getTurn() && getLocalPlayerFromGameState().getActionsTaken() == 0) {
            if (destinationPopUp == null) {
                destinationPopUp = new DestinationPopUp(gameState);
                destinationPopUp.showDuringGame(gameState, this);
                endTurn();
            }
        } else {
            systemMessage.setMessage("You cannot pick a card at this time.");
        }
    }

    public void endTurn() {
        if (destinationPopUp != null) {
            Platform.runLater(() -> {
                destinationPopUp.getStage().close();
                destinationPopUp = null;
            });
        }

        if (routePopUp != null) {
            Platform.runLater(() -> {
                routePopUp.getStage().close();
                routePopUp = null;
            });
        }

        if (playerTurnController.getTurn()) {
            getLocalPlayerFromGameState().setActionsTaken(2);
            checkIfTurnIsOver();
        }
    }

    public void endGame() {
        for (Player player : gameState.getPlayers()) {
            for (DestinationTicket ticket : player.getDestinationTickets()) {
                int points = ticket.getPoints();
                player.incrementPoints(isConnected(ticket, player) ? points : -points);
            }
            System.out.println("Points after tickets: " + player.getName() + " " + player.getPoints());
        }

        for (Rectangle rectangle : mapController.getRouteCellRectangleHashMap().values()) {
            rectangle.setFill(Color.TRANSPARENT);
        }

        HandModel handModel = HandModel.getInstance();
        handModel.setTrainCardsMap(new HashMap<String, Integer>());
        handModel.setDestinationTicketsInHand(new ArrayList<DestinationTicket>());

        turnTimerController.stopTimer();

        listenerRegistration.remove();
        MainState.firebaseService.getLobbyReference(MainState.roomCode).delete();
        MainState.player_uuid = null;
        MainState.roomCode = null;
        EndGameView endGameView = new EndGameView(gameState);
        endGameView.getStylesheets().add(MainState.menuCSS);
        MainState.primaryStage.setScene(new Scene(endGameView));
    }

    public void checkEndGame() {
        if (lastRound && lastActionTaken) {
            MainState.firebaseService.updateOngoingOfLobby(MainState.roomCode, false);
        }
    }

    // ===============================================================

    private void addTrainCardToPlayerInventoryInGameState(TrainCard trainCard) {
        getLocalPlayerFromGameState().addTrainCard(trainCard);
    }

    public Player getLocalPlayerFromGameState() {
        return gameState.getPlayer(MainState.player_uuid);
    }

    private Boolean isPlayerActionsTakenEquals2() {
        return (getLocalPlayerFromGameState().getActionsTaken() == 2);
    }

    private void incrementPlayerActionsTaken() {
        getLocalPlayerFromGameState().setActionsTaken(getLocalPlayerFromGameState().getActionsTaken() + 1);
    }

    public void registerObservers(GameView gameView) {
        cardsController.registerObserver(gameView);
        turnTimerController.registerObserver(gameView);
        bannerController.registerObserver(gameView);
        systemMessage.registerObserver(gameView);
    }

    private void checkIfTurnIsOver() {
        System.out.println("CHECK");
        if (isPlayerActionsTakenEquals2()) {
            // End turn
            System.out.println("NEXT TURN");

            if (lastRound) {
                lastActionTaken = true;
            }

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

    public void checkTrains() {
        if (!lastRound) {
            for (Player player : gameState.getPlayers()) {
                if (player.getTrains() <= 2) {
                    lastRound = true;
                    break;
                }
            }
        }
    }

    public Player getCurrentPlayer() {
        return playerTurnController.getCurrentPlayer(gameState);
    }

    /**
     * This method checks if the Cities on the given DestinationTicket have been connected by the given Player
     * It calls singleStep(), which uses recursive backtracking to find the path
     */
    public boolean isConnected(DestinationTicket ticket, Player player) {
        gameSetupService.addNeighborCities();
        boolean connected = singleStep(ticket.getFirstCity(), ticket.getSecondCity(), player);
        System.out.println(ticket.getFirstCity().getName() + " " + ticket.getSecondCity().getName() + " " + connected);
        gameSetupService.removeNeighborCities();
        return connected;
    }

    /**
     * This method runs a single step in the pathfinding algorithm using backtracking.
     * It checks all neighbors of the currentCity, and if the player has built a Route from
     * currentCity to the neighbor, this method calls itself again, but now with the
     * neighbor City as the new currentCity. This way all possibilities to connect any two given
     * Cities are tried
     * @param currentCity     City that we are at to check for a connection to destinationCity
     * @param destinationCity City that we are looking for a connection to
     * @param player          Player that we are checking for if they have a connection between the two Cities
     * @return true if there is a connection from the initial currentCity to the destinationCity, false otherwise
     */

    private boolean singleStep(City currentCity, City destinationCity, Player player) {
        // Accept case: We found the destination city.
        if (currentCity.equals(destinationCity)) {
            return true;
        }

        // Reject case: We already visited this city.
        if (currentCity.isVisited()) {
            return false;
        }

        // Backtracking step
        // Make a note that we visited this City, then try to go to each neighboring city.
        // (Janky hack because Firebase)
        for (City city : gameSetupService.getCities()) {
            if (currentCity.equals(city)) {
                currentCity.setNeighborCities(city.getNeighborCities());
                city.setVisited(true);
                currentCity.setVisited(true);
                break;
            }
        }

        for (City neighbor : currentCity.getNeighborCities()) {
            for (Route route : player.getClaimedRoutes()) {
                // If the player has built a route from currentCity to neighbor,
                // run the function again with neighbor as the new currentCity.
                boolean connectedAToB = route.getFirstCity().equals(currentCity) && route.getSecondCity().equals(neighbor);
                boolean connectedBToA = route.getFirstCity().equals(neighbor) && route.getSecondCity().equals(currentCity);
                if (connectedAToB || connectedBToA) {
                    if (singleStep(neighbor, destinationCity, player)) {
                        return true;
                    }
                }
            }
        }

        // Dead end: This location can't be part of the solution.
        // Unmark the location and go back to previous step.
        for (City city : gameSetupService.getCities()) {
            if (currentCity.equals(city)) {
                city.setVisited(false);
                currentCity.setVisited(true);
                break;
            }
        }
        return false;
    }
}
