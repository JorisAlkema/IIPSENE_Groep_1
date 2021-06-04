package Controller;

import App.MainState;
import Model.GameState;
import Model.TrainCard;
import View.CardView;
import com.google.cloud.firestore.ListenerRegistration;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Random;

public class CardController {
    private CardView cardView;
    private ListenerRegistration listenerRegistration;
    public CardController(CardView cardView) {
        this.cardView = cardView;

        // If host generate decks and put on firebase and show
        if (MainState.player.getHost()) {
            GameState gameState = MainState.firebaseService.getGameState(MainState.roomCode);
            ArrayList<TrainCard> closedCards = generateClosedDeck();
            gameState.setOpenDeck(generateOpenDeck(closedCards));
            gameState.setClosedDeck(closedCards);
            MainState.firebaseService.updateGameState(MainState.roomCode, gameState);
        }

        attachListener();
    }

    // Pick closed card()
    public void pickClosedCard() {
        TrainCard closedCard = getRandomCard();
        System.out.println(String.format("Closed card picked, color: %s", closedCard.getColor()));
        // Do smt with the card?
    }

    // Pick open card and return new open card
    public void pickOpenCard(int index) {
        GameState gameState = getGameState();
        ArrayList<TrainCard> openCards = gameState.getOpenDeck();

        TrainCard openCard = openCards.get(index);
        System.out.println(String.format("Open card picked, color: %s", openCard.getColor()));
        if (openCard.getColor() == "LOCO"){

        }

        // Remove picked opencard
        // Get a new open card from the closed cards
        // and update the firebase
        openCards.remove(index);
        TrainCard newOpenCard = getRandomCard();
        openCards.add(newOpenCard);
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
        GameState gameState = MainState.firebaseService.getGameState(MainState.roomCode);
        while (gameState.getClosedDeck() == null || gameState.getOpenDeck() == null) {
            gameState = MainState.firebaseService.getGameState(MainState.roomCode);
        }
        return gameState;
    }

    private TrainCard getRandomCard() {
        GameState gameState = MainState.firebaseService.getGameState(MainState.roomCode);
        ArrayList<TrainCard> closedDeck = gameState.getClosedDeck();
        TrainCard randomCard = closedDeck.get(new Random().nextInt(closedDeck.size()));
        closedDeck.remove(randomCard);
        gameState.setClosedDeck(closedDeck);
        MainState.firebaseService.updateGameState(MainState.roomCode, gameState);
        return randomCard;
    }

    public void attachListener() {
        listenerRegistration = MainState.firebaseService.getRoomReference(MainState.roomCode).addSnapshotListener((document, e) -> {
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
