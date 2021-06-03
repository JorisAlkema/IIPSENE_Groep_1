package Model;

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

    public TrainCardDeck() {
        trainCards = generateDeck();
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

     public TrainCard getRandomCard(){
        Random rand = new Random();
        TrainCard randomCard = trainCards.get(rand.nextInt(trainCards.size()));
        trainCards.remove(randomCard);
        return randomCard;
     }
}
