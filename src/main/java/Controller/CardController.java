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
        if (gameController.getPlayerTurnController().getTurn()) {
            GameState gameState = getGameState();
            TrainCard closedCard = getRandomCard(gameState);
            MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
            System.out.println(String.format("Closed card picked, color: %s", closedCard.getColor()));
            // Do smt with the card?
        }
    }

    // Pick open card and return new open card
    public Boolean pickOpenCard(int index) {
        if (gameController.getPlayerTurnController().getTurn()) {
            GameState gameState = getGameState();
            ArrayList<TrainCard> openCards = gameState.getOpenDeck();

            TrainCard pickedCard = openCards.get(index);
            Player player = gameState.getPlayer(MainState.player_uuid);

            if (player.getActionsTaken() == 1 && pickedCard.getColor().equals("LOCO")) {
                System.out.println("you cannot draw a LOCO");
                return false;
            }

            player.setActionsTaken(player.getActionsTaken() + 1);
            player.addTrainCard(pickedCard);
            System.out.printf("Open card picked, color: %s%n", pickedCard.getColor());

            openCards.remove(index);
            openCards.add(getRandomCard(gameState));

            // FIXME
            // In this if statement, gameController.endTurn() updates the GameState
            // After the if statement, gameState is updated as well
            // And also there's a SnapShotListener listening for OpenDeck changes
            // This results in weird behavior when adding open card to hand if that card
            // is the second card drawn that turn
            if (pickedCard.getColor().equals("LOCO") || player.getActionsTaken() >= 2) {
                System.out.println("Turn ended!");
                gameController.endTurn();
                player.setActionsTaken(0);
            }

            MainState.firebaseService.updateGameStateOfLobby(MainState.roomCode, gameState);
            return true;
        }
        return false;
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
                try {
                    if (gameState.getOpenDeck() != null) {
                        cardView.createButtons(gameState.getOpenDeck());
                    }
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            });
        });
    }

    public void detachListener() {
        listenerRegistration.remove();
    }
}
