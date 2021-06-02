package View;

import Model.Player;
import Model.TrainCard;
import Model.TrainCardDeck;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CardView extends VBox {

    public CardView() {
        super();
        displayCards();
    }

    public void displayCards(){
        setPadding(new Insets(30));
        Button getTrainCard = new Button("Get closed train card");
        getChildren().addAll(getTrainCard);

        TrainCardDeck app = new TrainCardDeck();
        ArrayList<TrainCard> deck = app.generateDeck();

        getTrainCard.setOnAction(e -> {
            //todo: give the player a card app.getRandomCard(deck);
        });

    }
}
