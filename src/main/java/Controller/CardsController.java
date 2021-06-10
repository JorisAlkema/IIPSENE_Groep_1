package Controller;

import App.MainState;
import Model.Cards;
import Model.GameState;
import Model.Player;
import Model.TrainCard;
import Observers.CardsObserver;

import java.util.ArrayList;
import java.util.Random;

public class CardsController {
    private final Cards cards = new Cards();

    public TrainCard pickClosedCard(GameState gameState) {
        TrainCard pickedClosedCard = getRandomCard(gameState);
        System.out.println("Closed card picked, color: " + pickedClosedCard.getColor());
        return pickedClosedCard;
    }

    // Return clicked open card and generate new open card to take its place
    public TrainCard pickOpenCard(GameState gameState, int index) throws Exception {
        ArrayList<TrainCard> openCards = gameState.getOpenDeck();
        TrainCard pickedCard = openCards.get(index);
        Player player = gameState.getPlayer(MainState.player_uuid);

        if (player.getActionsTaken() == 1 && pickedCard.getColor().equals("LOCO")) {
            throw new Exception("you cannot draw a LOCO");
        }

        // Replace open card
        openCards.remove(index);
        openCards.add(getRandomCard(gameState));

        notifyObservers(openCards);
        return pickedCard;
    }

    public ArrayList<TrainCard> generateClosedDeck() {
        String[] colors = {"PURPLE", "WHITE", "BLUE", "YELLOW", "ORANGE", "BLACK", "RED", "GREEN"};
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
        while (openCards.size() < 5) {
            TrainCard randomCard = closedCards.get(new Random().nextInt(closedCards.size()));
            closedCards.remove(randomCard);
            openCards.add(randomCard);
        }
        return openCards;
    }

    private TrainCard getRandomCard(GameState gameState) {
        ArrayList<TrainCard> closedDeck = gameState.getClosedDeck();
        System.out.println(">>>> Before shuffle" + closedDeck.size());
        if (closedDeck.size() == 0) {
            closedDeck = reshuffleCards(gameState);
        }
        System.out.println(">>>> After shuffle" + closedDeck.size());
        TrainCard randomCard = closedDeck.get(new Random().nextInt(closedDeck.size()));
        closedDeck.remove(randomCard);
        gameState.setClosedDeck(closedDeck);
        return randomCard;
    }

    private ArrayList<TrainCard> reshuffleCards(GameState gameState) {
        ArrayList<TrainCard> newClosedDeck;
        ArrayList<TrainCard> cardsToRemove = new ArrayList<>(gameState.getOpenDeck());
        for (Player player : gameState.getPlayers()) {
            cardsToRemove.addAll(player.getTrainCards());
        }
        newClosedDeck = generateClosedDeck();
        for (TrainCard trainCard : cardsToRemove) {
            newClosedDeck.remove(trainCard);
        }
        return newClosedDeck;
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
