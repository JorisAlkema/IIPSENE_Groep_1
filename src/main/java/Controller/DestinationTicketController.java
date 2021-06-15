package Controller;

import Model.DestinationTicket;
import Model.DestinationTicketDeck;

import java.util.ArrayList;
import java.util.Random;

public class DestinationTicketController {
    private final DestinationTicketDeck destinationTicketDeck;

    public DestinationTicketController(ArrayList<DestinationTicket> destinationTickets) {
        this.destinationTicketDeck = new DestinationTicketDeck(destinationTickets);
    }

    private DestinationTicket drawSingleCard(String type) {
        Random random = new Random();
        int deckSize = destinationTicketDeck.getDestinationTickets().size();
        int shortDeckSize = destinationTicketDeck.getShortDestinationTickets().size();

        if (shortDeckSize == 0) {
            return null;
        }

        DestinationTicket randomTicket = destinationTicketDeck.draw(random.nextInt(deckSize));
        while (!randomTicket.getType().equals(type)) {
            returnCardToDeck(randomTicket);
            randomTicket = destinationTicketDeck.draw(random.nextInt(deckSize));
        }
        return randomTicket;
    }

    public void returnCardToDeck(DestinationTicket ticket) {
        this.destinationTicketDeck.addCardToDeck(ticket);
    }

    public ArrayList<DestinationTicket> drawTickets(boolean drawExtra) {
        ArrayList<DestinationTicket> randomDestinationTickets = new ArrayList<>();
        for (int i =0;i<3;i++){
            DestinationTicket newCard;
            newCard = drawSingleCard("short");
            if (newCard == null){
                break;
            }
            randomDestinationTickets.add(newCard);
        }
        if (drawExtra) {
            randomDestinationTickets.add(drawSingleCard("long"));
        }
        return randomDestinationTickets;
    }
}
