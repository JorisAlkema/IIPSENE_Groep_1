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

    private DestinationTicket drawSingleCard(String type) {
        Random random = new Random();
        int deckSize = destinationTicketDeck.getDestinationTickets().size();
        DestinationTicket randomTicket = destinationTicketDeck.draw(random.nextInt(deckSize));
        while ( ! randomTicket.getType().equals(type)) {
            returnCardToDeck(randomTicket);
            randomTicket = destinationTicketDeck.draw(random.nextInt(deckSize));
        }
        return randomTicket;
    }

    public void returnCardToDeck(DestinationTicket ticket) {
        this.destinationTicketDeck.addCardToDeck(ticket);
    }

    public ArrayList<DestinationTicket> drawTickets(boolean drawExtra){
        ArrayList<DestinationTicket> randomDestinationTickets = new ArrayList<>();
        while (randomDestinationTickets.size() < 3){
            randomDestinationTickets.add(drawSingleCard("short"));
        }
        if (drawExtra) {
            randomDestinationTickets.add(drawSingleCard("long"));
        }
        return randomDestinationTickets;
    }
}
