package Observers;

import Model.DestinationTicket;

import java.util.ArrayList;
import java.util.HashMap;

public interface HandObserver {
    void update(HashMap<String, Integer> trainCardMap, ArrayList<String> ticketFileNamesSmall);
}
