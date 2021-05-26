package Model;

import java.util.ArrayList;

// The Player class represents a player in the game
public class Player {
    private String name;
    private String UUID;
    private String playerColor;
    private ArrayList<TrainCard> trainCards;
    private ArrayList<DestinationTicket> destinationTickets;
    private int points;
    private int trains;
    private ArrayList<Route> claimedRoutes;

    public Player(String name, String UUID, String playerColor, ArrayList<TrainCard> trainCards, ArrayList<DestinationTicket> destinationTickets, int points, int trains, ArrayList<Route> claimedRoutes) {
        this.name = name;
        this.UUID = UUID;
        this.playerColor = playerColor;
        this.trainCards = trainCards;
        this.destinationTickets = destinationTickets;
        this.points = points;
        this.trains = trains;
        this.claimedRoutes = claimedRoutes;
    }

    public ArrayList<TrainCard> getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(ArrayList<TrainCard> trainCards) {
        this.trainCards = trainCards;
    }
}
