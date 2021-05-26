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
    ArrayList<TrainCard> trainCards = new ArrayList<TrainCard>();
    ArrayList<TrainCard> openCards = new ArrayList<TrainCard>();
    String[] colors  = {"purple", "white", "blue", "yellow", "orange", "black", "red", "green"};

    public TrainCardDeck() {
    }

    public ArrayList<TrainCard> generateDeck() {

        for (String color : colors) {
            for (int i = 0; i < 12; i++) {
                trainCards.add(new TrainCard(color));
            }
        }
        for (int i = 0; i < 14; i++) {
            trainCards.add(new TrainCard("locomotive"));
        }
        return trainCards;
    }


     public TrainCard getRandomCard(ArrayList<TrainCard> array){
        Random rand = new Random();
        TrainCard randomCard = array.get(rand.nextInt(array.size()));
        array.remove(randomCard);
        return randomCard;
     }

    public ArrayList<TrainCard> fillOpenCards(ArrayList<TrainCard> deck) {

        while(openCards.size() < 5){
            openCards.add(getRandomCard(deck));
        }
        return openCards;
    }

    public static void main(String[] args){
        //to test functionality
        TrainCardDeck app = new TrainCardDeck();
        ArrayList<TrainCard> deck = app.generateDeck();
        for (TrainCard card : deck){
            System.out.println(card.getColor());
        }
        System.out.println(deck.size());
        System.out.println("random card:" + app.getRandomCard(deck).getColor());
        System.out.println(deck.size());
    }
}
