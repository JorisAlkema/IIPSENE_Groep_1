package Controller;

import App.MainState;
import Model.Cards;
import Model.GameState;
import Model.Player;
import Model.TrainCard;
import Observers.CardsObservable;
import Observers.CardsObserver;

import java.util.ArrayList;
import java.util.Random;

public class CardsController {
    private Cards cards = new Cards();

    public TrainCard pickClosedCard(GameState gameState) {
        TrainCard pickedClosedCard = getRandomCard(gameState);
        System.out.println(String.format("Closed card picked, color: %s", pickedClosedCard.getColor()));
        return pickedClosedCard;
    }
    // Pick open card and return new open card
    public TrainCard pickOpenCard(GameState gameState, int index) throws Exception {
        ArrayList<TrainCard> openCards = gameState.getOpenDeck();
        TrainCard pickedCard = openCards.get(index);
        Player player = gameState.getPlayer(MainState.player_uuid);

        if (player.getActionsTaken() == 1 && pickedCard.getColor().equals("LOCO")) {
            throw new Exception("you cannot draw a LOCO");
        }

        // Get random closed card and place it in the open cards deck
        openCards.remove(index);
        openCards.add(getRandomCard(gameState));

        notifyObservers(openCards);
        return pickedCard;
    }

    public ArrayList<TrainCard> generateClosedDeck() {
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

    public ArrayList<TrainCard> generateOpenDeck(ArrayList<TrainCard> closedCards) {

        ArrayList<TrainCard> openCards = new ArrayList<>();
        while(openCards.size() < 5){
            TrainCard randomCard = closedCards.get(new Random().nextInt(closedCards.size()));
            closedCards.remove(randomCard);
            openCards.add(randomCard);
        }
        return openCards;
    }

    private TrainCard getRandomCard(GameState gameState) {
        ArrayList<TrainCard> closedDeck = gameState.getClosedDeck();
        TrainCard randomCard = closedDeck.get(new Random().nextInt(closedDeck.size()));
        closedDeck.remove(randomCard);
        gameState.setClosedDeck(closedDeck);
        return randomCard;
    }


    public void registerObserver(CardsObserver observer) {
        cards.registerObserver(observer);
    }

    public void unregisterObserver(CardsObserver observer) {
        cards.unregisterObserver(observer);
    }

    public void notifyObservers(ArrayList<TrainCard> openCards) {
        cards.notifyObservers(openCards);
    }
}
