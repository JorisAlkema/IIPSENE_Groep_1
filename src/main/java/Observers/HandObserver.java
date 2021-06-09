package Observers;

import Model.DestinationTicket;
import Model.TrainCard;

import java.util.ArrayList;

public interface HandObserver {
    void update(ArrayList<TrainCard> trainCards, ArrayList<DestinationTicket> destinationTickets);
}
