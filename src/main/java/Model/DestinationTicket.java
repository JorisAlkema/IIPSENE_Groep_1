package Model;

// Instances of this class represent destination tickets. They have a filepath String referring to the corresponding
// png image, a list of Cities which they connect, a boolean which indicates whether the two Cities have been
// connected, and an amount of points for successfully connecting them.
// DestinationTickets have two types: long and short. At the start of the game each player is dealt one long and three
// short DestinationTickets, and has to keep at least two of them. After this, all DestinationTickets of the long type
// that are not kept in a Players hand are discarded; they are not added to the DestinationTicketDeck
public class DestinationTicket {
    private final City firstCity;
    private final City secondCity;
    private final int points;
    private final String type;
    private boolean connected;

    public DestinationTicket(City firstCity, City secondCity, int points, String type) {
        this.firstCity = firstCity;
        this.secondCity = secondCity;
        this.points = points;
        this.type = type;
        connected = false;
    }

    public String getFileName() {
        return "destination_tickets/eu-" + firstCity.getName().toLowerCase() + "-" + secondCity.getName().toLowerCase() + ".png";
    }

    public City getFirstCity() {
        return firstCity;
    }

    public City getSecondCity() {
        return secondCity;
    }

    public String getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
