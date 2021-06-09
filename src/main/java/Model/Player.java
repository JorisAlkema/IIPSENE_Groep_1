package Model;

import java.util.ArrayList;

// The Player class represents a player in the game
public class Player {
    private String name;
    private String UUID;
    private Boolean host;
    private String playerColor;
    private ArrayList<TrainCard> trainCards;
    private ArrayList<DestinationTicket> destinationTickets;
    private ArrayList<Route> claimedRoutes;
    private int points;
    private int trains;
    private boolean isTurn;
    private int actionsTaken;

    public Player(String name, String UUID, String playerColor, boolean isTurn) {
        this.name = name;
        this.UUID = UUID;
        this.playerColor = playerColor;
        this.isTurn = isTurn;
        this.trainCards = new ArrayList<>();
        this.destinationTickets = new ArrayList<>();
        this.claimedRoutes = new ArrayList<>();
        this.points = 0;
        this.trains = 45; // Default value according to rules
        this.actionsTaken = 0;
    }

    public Player(String name, String UUID, Boolean host) {
        this.name = name;
        this.UUID = UUID;
        this.host = host;
        this.trainCards = new ArrayList<>();
        this.destinationTickets = new ArrayList<>();
        this.claimedRoutes = new ArrayList<>();
        this.points = 0;
        this.trains = 45; // Default value according to rules
        this.actionsTaken = 0;
    }

    public Player() {

    }

    public void givePointForRouteSize(int routeLength) {
        switch (routeLength) {
            case 1: incrementPoints(1); break;
            case 2: incrementPoints(2); break;
            case 3: incrementPoints(4); break;
            case 4: incrementPoints(7); break;
            case 6: incrementPoints(15); break;
            case 8: incrementPoints(21); break;
            default: incrementPoints(0); break;
        }
    }

    public void addTrainCard(TrainCard trainCard){
        this.trainCards.add(trainCard);
        HandModel handModel = HandModel.getInstance();
        handModel.setTrainCards(this.trainCards);
    }

    public void addDestinationTicket(DestinationTicket destinationTicket){
        this.destinationTickets.add(destinationTicket);
    }

    public void incrementPoints(int points) {
        this.points += points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Boolean getHost() {
        return host;
    }

    public void setHost(Boolean host) {
        this.host = host;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public ArrayList<TrainCard> getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(ArrayList<TrainCard> trainCards) {
        this.trainCards = trainCards;
    }

    public ArrayList<DestinationTicket> getDestinationTickets() {
        return destinationTickets;
    }

    public void setDestinationTickets(ArrayList<DestinationTicket> destinationTickets) {
        this.destinationTickets = destinationTickets;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTrains() {
        return trains;
    }

    public void setTrains(int trains) {
        this.trains = trains;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }

    public int getActionsTaken() {
        return actionsTaken;
    }

    public void setActionsTaken(int actionsTaken) {
        this.actionsTaken = actionsTaken;
    }

    public ArrayList<Route> getClaimedRoutes() {
        return claimedRoutes;
    }

    public void setClaimedRoutes(ArrayList<Route> claimedRoutes) {
        this.claimedRoutes = claimedRoutes;
    }
}
