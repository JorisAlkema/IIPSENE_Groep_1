package Model;

// Instances of this class represent destination tickets. They have a filepath String referring to the corresponding
// png image, a list of Cities which they connect, a boolean which indicates whether the two Cities have been
// connected, and an amount of points for successfully connecting them.
// DestinationTickets have two types: long and short. At the start of the game each player is dealt one long and three
// short DestinationTickets, and has to keep at least two of them. After this, all DestinationTickets of the long type
// that are not kept in a Players hand are discarded; they are not added to the DestinationTicketDeck
// TODO: determine in which class the pathfinding algorithm which calculate if two cities are connected should be
public class DestinationTicket {
    private final City firstCity;
    private final City secondCity;
    private final String type;
    private final int points;
    private boolean connected;

    public DestinationTicket(City firstCity, City secondCity, String type, int points) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.points = points;
        this.type = type;
        connected = false;
    }

    public DestinationTicket(City firstCity, City secondCity, String type, int points, boolean connected) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.points = points;
        this.type = type;
        this.connected = connected;
    }
}
