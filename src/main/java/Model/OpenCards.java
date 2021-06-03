package Model;

import java.util.ArrayList;

public class OpenCards {
    public ArrayList<TrainCard> openCards;

    public OpenCards() {
        openCards = new ArrayList<TrainCard>();
    }

    public ArrayList<TrainCard> getOpenCards() {
        return openCards;
    }

    public void setOpenCards(ArrayList<TrainCard> openCards) {
        this.openCards = openCards;
    }


}
