package Observers;

import Model.DestinationTicket;
import Model.TrainCard;

import java.util.ArrayList;
import java.util.HashMap;

public interface HandObserver {
    void update(HashMap<String, Integer> trainCardMap, ArrayList<DestinationTicket> destinationTickets);
}
