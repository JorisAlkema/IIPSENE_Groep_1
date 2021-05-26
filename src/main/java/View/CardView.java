package View;

import Model.OpenCards;
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

        TrainCardDeck deck = new TrainCardDeck();
        OpenCards openCards = new OpenCards(deck);

        getTrainCard.setOnAction(e -> {
            //todo: give the player a card app.getRandomCard(deck);
        });
        Button[] buttons = new Button[5];

        //forloop die knoppen maakt van de opencardsarray
        for(int i =0; i < openCards.getOpenCards().size(); i++) {
            buttons[i] = new Button(openCards.getOpenCards().get(i).getColor());
            getChildren().addAll(buttons);
        }
    }
}
