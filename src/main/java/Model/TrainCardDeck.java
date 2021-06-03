package Model;

import App.MainState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

// TrainCardDeck represents the closed deck of TrainCards next to the board. This class should allow a Player to draw
// face-down TrainCards, it should replace face-up TrainCards when they are taken and it should draw three TrainCards
// when a Player tries to build a tunnel Route.
// A complete deck contains: 12 cards of each color purple, white, blue, yellow, orange, black, red, green and 14
// locomotive cards.
// Possible methods: drawTrainCard(), shuffle(), ...
// TODO: Think about implementation type of the deck. List<TrainCard>, Map<TrainCard, Integer> or something else?
public class TrainCardDeck {
    ArrayList<TrainCard> trainCards;
    String[] colors  = {"PURPLE", "WHITE", "BLUE", "YELLOW", "ORANGE", "BLACK", "RED", "GREEN"};
    GameState gameState = MainState.firebaseService.getGameState(MainState.roomCode);
    public TrainCardDeck() {
        trainCards = generateDeck();
        gameState.setClosedDeck(trainCards);
        MainState.firebaseService.updateGameState(MainState.roomCode,gameState);
    }

    private ArrayList<TrainCard> generateDeck() {
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

    private ArrayList<TrainCard> reshuffleDeck(){
        ArrayList<TrainCard> trainCards = new ArrayList<>();
        //TODO: use the old cards and shuffle them into a new deck.
        return trainCards;
    }

    public TrainCard getRandomCard(){
        Random rand = new Random();
        TrainCard randomCard = gameState.getClosedDeck().get(rand.nextInt(trainCards.size()));
        gameState.getClosedDeck().remove(randomCard);
        MainState.firebaseService.updateGameState(MainState.roomCode,gameState);
        return randomCard;
    }
}
