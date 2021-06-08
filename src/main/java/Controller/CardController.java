package Controller;

import App.MainState;
import Model.GameState;
import Model.Player;
import Model.TrainCard;
import View.CardView;
import com.google.cloud.firestore.ListenerRegistration;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Random;

public class CardController {
    private CardView cardView;
    private ListenerRegistration listenerRegistration;
    GameController gameController;

    public CardController(CardView cardView,GameController gameController) {
        this.cardView = cardView;
        this.gameController = gameController;

        // If host generate decks and put on firebase and show
        if (MainState.firebaseService.getPlayerFromLobby(MainState.roomCode, MainState.player_uuid).getHost()) {
            GameState gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
            ArrayList<TrainCard> closedCards = generateClosedDeck();
            gameState.setOpenDeck(generateOpenDeck(closedCards));
            gameState.setClosedDeck(closedCards);
            MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
        }

        attachListener();
    }

    // Pick closed card()
    public void pickClosedCard() {
        GameState gameState = getGameState();
        TrainCard closedCard = getRandomCard(gameState);
        MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
        System.out.println(String.format("Closed card picked, color: %s", closedCard.getColor()));
        // Do smt with the card?
    }

    // Pick open card and return new open card
    public Boolean pickOpenCard(int index) {
        GameState gameState = getGameState();
        ArrayList<TrainCard> openCards = gameState.getOpenDeck();

        TrainCard pickedCard = openCards.get(index);
        Player client = gameState.getPlayer(MainState.player_uuid);

        if(client.getActionsTaken() == 1 && pickedCard.getColor().equals("LOCO")){
            System.out.println("you cannot draw a LOCO");
            return false;
        }

        client.setActionsTaken(client.getActionsTaken() + 1);

        if(pickedCard.getColor().equals("LOCO") || client.getActionsTaken() >= 2) {
            gameController.endTurn(client);
            client.addTrainCard(pickedCard);
            System.out.println(client.getTrainCards());
            System.out.println("turn ended!");
            client.setActionsTaken(0);
        }

        if(client.getActionsTaken() >= 1) {
            client.addTrainCard(pickedCard);
        }

        System.out.println(String.format("Open card picked, color: %s", pickedCard.getColor()));
        // Remove picked opencard
        // Get a new open card from the closed cards
        // and update the firebase
        openCards.remove(index);
        TrainCard newOpenCard = getRandomCard(gameState);
        openCards.add(newOpenCard);
        MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
        return true;
    }

    private ArrayList<TrainCard> generateClosedDeck() {
        String[] colors  = {"PURPLE", "WHITE", "BLUE", "YELLOW", "ORANGE", "BLACK", "RED", "GREEN"};
        ArrayList<TrainCard> trainCards = new ArrayList<>();

        for (String color : colors) {
            for (int i = 0; i < 12; i++) {
                trainCards.add(new TrainCard(color));
            }
        }
        for (int i = 0; i < 14; i++) {
            trainCards.add(new TrainCard("LOCO"));
        }
        return trainCards;
    }

    private ArrayList<TrainCard> generateOpenDeck(ArrayList<TrainCard> closedCards) {

        ArrayList<TrainCard> openCards = new ArrayList<>();
        while(openCards.size() < 5){
            TrainCard randomCard = closedCards.get(new Random().nextInt(closedCards.size()));
            closedCards.remove(randomCard);
            openCards.add(randomCard);
        }
        return openCards;
    }

    private GameState getGameState() {
        GameState gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
        while (gameState.getClosedDeck() == null || gameState.getOpenDeck() == null) {
            gameState = MainState.firebaseService.getGameStateOfLobby(MainState.roomCode);
        }
        return gameState;
    }

    private TrainCard getRandomCard(GameState gameState) {
        ArrayList<TrainCard> closedDeck = gameState.getClosedDeck();
        TrainCard randomCard = closedDeck.get(new Random().nextInt(closedDeck.size()));
        closedDeck.remove(randomCard);
        gameState.setClosedDeck(closedDeck);
        return randomCard;
    }

    public void attachListener() {
        listenerRegistration = MainState.firebaseService.getLobbyReference(MainState.roomCode).addSnapshotListener((document, e) -> {
            Platform.runLater(() -> {
                GameState gameState = document.toObject(GameState.class);
                if (gameState.getOpenDeck() != null) {
                    cardView.createButtons(gameState.getOpenDeck());
                }
            });
        });
    }

    public void detachListener() {
        listenerRegistration.remove();
    }
}
