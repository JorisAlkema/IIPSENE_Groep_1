package Model;

import java.util.ArrayList;

public class OpenCards {
    ArrayList<TrainCard> openCards = new ArrayList<TrainCard>();
    private TrainCardDeck deck;

    public OpenCards(TrainCardDeck deck) {
        this.deck = deck;
        openCards = fillOpenCards();
    }

    public ArrayList<TrainCard> fillOpenCards() {
        while(openCards.size() < 5){
            openCards.add(deck.getRandomCard());
        }
        return openCards;
    }

    public ArrayList<TrainCard> getOpenCards() {
        return openCards;
    }


}
