package Model;

import java.util.ArrayList;


// DestinationTicketDeck represents the closed deck of DestinationTickets next to the board.
// This class should allow a Player to draw three DestinationTickets and force them to keep at least one of them.
// Possible methods: drawDestinationTickets(), shuffle(), ...
public class DestinationTicketDeck {
    private ArrayList<DestinationTicket> destinationTickets;

    public DestinationTicketDeck(ArrayList<DestinationTicket> destinationTickets) {
        this.destinationTickets = destinationTickets;
    }

    public ArrayList<DestinationTicket> getDestinationTickets() {
        return destinationTickets;
    }
}
