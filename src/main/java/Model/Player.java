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
    private int points;
    private int trains;
    private boolean isTurn;
    private int actionsTaken;
    private ArrayList<Route> claimedRoutes;

    public Player(String name, String UUID, String playerColor, ArrayList<TrainCard> trainCards, ArrayList<DestinationTicket> destinationTickets, int points, int trains, boolean isTurn, int actionsTaken, ArrayList<Route> claimedRoutes) {
        this.name = name;
        this.UUID = UUID;
        this.playerColor = playerColor;
        this.trainCards = trainCards;
        this.destinationTickets = destinationTickets;
        this.points = points;
        this.trains = trains;
        this.isTurn = isTurn;
        this.actionsTaken = actionsTaken;
        this.claimedRoutes = claimedRoutes;
    }

    public Player(String name, String UUID, Boolean host) {
        this.name = name;
        this.UUID = UUID;
        this.host = host;
    }

    public Player() {

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
