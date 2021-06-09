package Model;

import App.MainState;
import Observers.HandObservable;
import Observers.HandObserver;

import java.util.ArrayList;

public class HandModel implements HandObservable {
    private ArrayList<TrainCard> trainCardsInHand;
    private ArrayList<DestinationTicket> destinationTicketsInHand;
    private ArrayList<HandObserver> observers;
    static HandModel handModel;

    public HandModel() {
        Player localPlayer = MainState.getLocalPlayer();
        trainCardsInHand = localPlayer.getTrainCards();
        destinationTicketsInHand = localPlayer.getDestinationTickets();
        observers = new ArrayList<>();
    }

    public static HandModel getInstance() {
        if (handModel == null) {
            handModel = new HandModel();
        }
        return handModel;
    }

    public void setTrainCards(ArrayList<TrainCard> trainCards) {
        trainCardsInHand = trainCards;
        notifyObservers();
    }

    public void setDestinationTicketsInHand(ArrayList<DestinationTicket> destinationTicketsInHand) {
        this.destinationTicketsInHand = destinationTicketsInHand;
        notifyObservers();
    }

    @Override
    public void registerObserver(HandObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(HandObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (HandObserver observer : observers) {
            observer.update(trainCardsInHand, destinationTicketsInHand);
        }
    }
}
