package Model;

import App.MainState;

import java.util.ArrayList;

public class OpenCards {
    public ArrayList<TrainCard> openCards = new ArrayList<TrainCard>();
    private TrainCardDeck deck;


    public OpenCards(TrainCardDeck deck) {
        GameState gameState = MainState.firebaseService.getGameState(MainState.roomCode);
        this.deck = deck;
        openCards = fillOpenCards();
        gameState.setOpenDeck(openCards);
        MainState.firebaseService.updateGameState(MainState.roomCode,gameState);
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
