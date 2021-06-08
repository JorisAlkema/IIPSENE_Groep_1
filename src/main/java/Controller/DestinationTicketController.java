package Controller;

import Model.DestinationTicket;
import Model.DestinationTicketDeck;
import Model.Player;
import Service.GameSetupService;
import View.DestinationTicketView;
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

        if(drawExtra){
            Random rand = new Random();
            DestinationTicket randomTicket = ticketDeck.get(rand.nextInt(ticketDeck.size()));
            if(randomTicket.getType().equals("long")){
                randomDestinationTickets.add(randomTicket);
            }
        }
        return randomDestinationTickets;
    }


    // TODO: reference to player, then use player.getDestinationTickets().get(0).getFileName()

}
