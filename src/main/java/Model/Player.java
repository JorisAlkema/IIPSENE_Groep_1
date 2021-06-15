package Model;

import java.util.ArrayList;
import java.util.HashMap;

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
    private int actionsTaken;

    private boolean isTurn;

    public Player(String name, String UUID, Boolean host) {
        this.name = name;
        this.UUID = UUID;
        this.host = host;
        this.trainCards = new ArrayList<>();
        this.destinationTickets = new ArrayList<>();
        this.claimedRoutes = new ArrayList<>();
        this.points = 0;
        this.trains = 5; // Default value according to rules
        this.actionsTaken = 0;
    }

    // Allow Firebase to create a new instance of the object with an empty constructor,
    // which will be filled in using reflection.
    public Player() {
    }

    public void addTrainCard(TrainCard trainCard) {
        this.trainCards.add(trainCard);
        HandModel handModel = HandModel.getInstance();
        handModel.setTrainCardsMap(trainCardsAsMap());
    }

    public void addDestinationTicket(DestinationTicket destinationTicket) {
        this.destinationTickets.add(destinationTicket);
        HandModel handModel = HandModel.getInstance();
        handModel.setDestinationTicketsInHand(this.destinationTickets);
    }

    public HashMap<String, Integer> trainCardsAsMap() {
        HashMap<String, Integer> map = new HashMap<>();
        for (TrainCard trainCard : this.trainCards) {
            String color = trainCard.getColor();
            if (map.containsKey(color)) {
                map.put(color, map.get(color) + 1);
            } else {
                map.put(color, 1);
            }
        }
        return map;
    }

    public void incrementPoints(int points) {
        this.points += points;
    }

    public void decrementTrains(int routeLength) {
        this.trains -= routeLength;
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

    public void addClaimedRoute(Route route) {
        this.claimedRoutes.add(route);
    }

    public void setClaimedRoutes(ArrayList<Route> claimedRoutes) {
        this.claimedRoutes = claimedRoutes;
    }
}
