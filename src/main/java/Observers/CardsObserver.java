package Observers;

import Model.TrainCard;

import java.util.ArrayList;

public interface CardsObserver {
    void update(ArrayList<TrainCard> openCards);
}
