package Controller;

import Model.OpenCards;
import Model.TrainCard;
import Model.TrainCardDeck;
import View.CardView;

public class CardController {
    private TrainCardDeck deck;
    private OpenCards openDeck;
    private CardView cardView;

    public CardController(CardView cardView) {
        this.cardView = cardView;
        this.deck = new TrainCardDeck();
        this.openDeck = new OpenCards(deck);
    }
    public TrainCard getRandomCard(){
        return deck.getRandomCard();
    }

    public TrainCardDeck getDeck() {
        return deck;
    }

    public OpenCards getOpenDeck() {
        return openDeck;
    }
}
