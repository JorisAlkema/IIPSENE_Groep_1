package Model;

import java.util.ArrayList;

// The Player class represents a player in the game
public class Player {
    private String name;
    private String UUID;
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

    public boolean claimRoute(Route route, String color) {
        // Route already claimed
        if (route.getOwner() != null) {
            return false;
        }
        String routeColor = route.getColor();
        // If the Route is grey, check for cards of the given color
        if (routeColor.equals("GREY")) {
            routeColor = color;
        }
        String type = route.getType();
        int requiredLocos = route.getRequiredLocomotives();
        int routeLength = route.getLength();
        ArrayList<TrainCard> correctColorCards = new ArrayList<>();
        ArrayList<TrainCard> locosInHand = new ArrayList<>();
        for (TrainCard trainCard : this.trainCards) {
            if (trainCard.getColor().equals(routeColor)) {
                correctColorCards.add(trainCard);
            } else if (trainCard.getColor().equals("LOCO")) {
                locosInHand.add(trainCard);
            }
            if (correctColorCards.size() >= routeLength && locosInHand.size() >= requiredLocos) {
                break;
            }
        }
        // Not enough cards of the right color
        if (correctColorCards.size() + locosInHand.size() < routeLength) {
            return false;
        }
        if (type.equals("TUNNEL")) {
            // TODO
        }
        if (type.equals("FERRY") && locosInHand.size() < requiredLocos) {
            return false;
        }
        // Remove cards from hand
        int cardsToRemove = routeLength;
        for (TrainCard trainCard : correctColorCards) {
            if (cardsToRemove > 0) {
                this.trainCards.remove(trainCard);
                cardsToRemove--;
            }
        }
        for (TrainCard trainCard : locosInHand) {
            if (cardsToRemove > 0) {
                this.trainCards.remove(trainCard);
                cardsToRemove--;
            }
        }

        claimedRoutes.add(route);
        route.setOwner(this);

        return true;
    }

    public String getName() {
        return name;
    }

    public ArrayList<TrainCard> getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(ArrayList<TrainCard> trainCards) {
        this.trainCards = trainCards;
    }

    public boolean getTurn() {
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

    public ArrayList<TrainCard> getTrainCards(String color) {
        ArrayList<TrainCard> cards = new ArrayList<>();
        for (TrainCard trainCard : this.trainCards) {
            if (trainCard.getColor().equals(color)) {
                cards.add(trainCard);
            }
        }
        return cards;
    }
}
