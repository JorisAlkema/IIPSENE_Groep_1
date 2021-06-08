package Controller;

import Model.DestinationTicket;
import Model.DestinationTicketDeck;
import java.util.Random;

import java.util.ArrayList;

public class DestinationTicketController {
    private DestinationTicketDeck destinationTicketDeck;

    public DestinationTicketController(ArrayList<DestinationTicket> destinationTickets) {
        this.destinationTicketDeck = new DestinationTicketDeck(destinationTickets);
    }

    public ArrayList<DestinationTicket> getDestinationTickets(boolean drawExtra){
        ArrayList<DestinationTicket> randomDestinationTickets = new ArrayList<DestinationTicket>();
        ArrayList<DestinationTicket> ticketDeck = destinationTicketDeck.getDestinationTickets();

        while (randomDestinationTickets.size() < 3){
            Random rand = new Random();
            DestinationTicket randomTicket = ticketDeck.get(rand.nextInt(ticketDeck.size()));
            if(randomTicket.getType().equals("short")){
                randomDestinationTickets.add(randomTicket);
            }
        }

        while(drawExtra && randomDestinationTickets.size() < 4){
            Random rand = new Random();
            DestinationTicket randomTicket = ticketDeck.get(rand.nextInt(ticketDeck.size()));
            if(randomTicket.getType().equals("long")){
                randomDestinationTickets.add(randomTicket);
            }
        }
        return randomDestinationTickets;
    }
}
