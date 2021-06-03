package Model;

// TrainCard represents a train card in the game and is used to claim routes. TrainCards are found in the Players
// hand, in the TrainCardDeck or as one of the five open TrainCards next to the game board.
// TrainCard only has a color.
public class TrainCard {
    // Color can be PURPLE, WHITE, BLUE, YELLOW, ORANGE, BLACK, RED, GREEN or LOCO
    private String color;

    public TrainCard() {

    }

    public TrainCard(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
